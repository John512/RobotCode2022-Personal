package frc.robot.autons;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static frc.robot.Constants.Auton.HighGear.*;
import static frc.robot.Constants.Auton.LowGear.*;
import static frc.robot.Constants.Auton.driveKinematics;
import static frc.robot.Constants.DriveTrain.*;

/***
 * Generates an array list of Ramsete Commands for use in AutonRoutine. The first method, getTrajectory, creates a path based on a file
 * created by Pathweaver and then creates a trajectory based on the path. The second method, getAutonCommands, creates an
 * arraylist of RamseteCommands based on the each trajectory and PID constants. See the WPI First page for more information
 * about RamseteCommands
 */

public class AutonGenerator {

    ShiftState mShiftState;
    Drivetrain mDrivetrain;
    RamseteController mRamseteController = new RamseteController(Constants.Auton.ramseteB, Constants.Auton.ramseteZeta);
    SimpleMotorFeedforward mFeedForward;
    PIDController leftPID, rightPID;

    //Fills an array with trajectories based on a String array of the paths and returns it
    public Trajectory getTrajectory(String pathing){
//        ArrayList<Trajectory> trajectories = new ArrayList<>();
        Trajectory trajectory;
        try {
            Path path = Filesystem.getDeployDirectory().toPath().resolve("opt/output/" + pathing + ".wpilib.json");
            trajectory = (TrajectoryUtil.fromPathweaverJson(path));
            System.out.println("Added trajectory " + pathing + " to array");
        } catch (IOException e){
            System.out.println("Couldn't find trajectory path");
            e.printStackTrace();
            return null;
        }
        if (trajectory != null){
            System.out.println("Retrieved trajectory");
        }
        return trajectory;
    }

    //Fills an array with Ramsete commands based on a String array of paths and returns it
    public ArrayList<RamseteCommand> getAutonCommands(String[] pathing, Drivetrain subsystem) {
        ArrayList<RamseteCommand> commands = new ArrayList<>();
        mRamseteController.setEnabled(true);
        mDrivetrain = subsystem;
        ShiftState mShiftState = mDrivetrain.getShiftState();
        mShiftState = ShiftState.LOW_GEAR;

        switch (mShiftState) {
            case LOW_GEAR:
                mFeedForward = new SimpleMotorFeedforward(Low_ks, Low_kv, Low_ka);
                leftPID = new PIDController(Low_kP, 0, 0);
                rightPID = new PIDController(Low_kP, 0, 0);
                break;
            case HIGH_GEAR:
                mFeedForward = new SimpleMotorFeedforward(High_ks, High_kv, High_ka);
                leftPID = new PIDController(High_kP, 0, 0);
                rightPID = new PIDController(High_kP, 0, 0);
                break;
            default:
                System.out.println("Error! No Shift State");
                break;
        }

        for (int i = 0; i < pathing.length; i++) {
            System.out.println("Adding trajectory " + i + " to commands");
            Trajectory trajectory = getTrajectory(pathing[i]);
            if (trajectory == null) {
                System.out.println("Trajectory " + i + " not found");
                return null;
            }
            commands.add(
                    new RamseteCommand(
                            trajectory,
                            mDrivetrain::getPose,
                            mRamseteController,
                            mFeedForward,
                            driveKinematics,
                            mDrivetrain::getWheelSpeeds,
                            leftPID, rightPID,
                            (leftVolts, rightVolts) -> {
                                mDrivetrain.tankDriveVolts(leftVolts, rightVolts);
                            },
                            mDrivetrain));
        }
        return commands;
    }
}
