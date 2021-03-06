package frc.robot.limelightvision;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utility.MathEqs;

public class LimelightDistanceCommand extends CommandBase {

    /***
     * Work in Progress class for adjusting distance to the target
     */

    private Drivetrain mDrivetrain;
    private VPLimelight mVision;

    private double yOffset, speed, goalDistance, initDistance, goalTravel, actualTravel, remainDistance, buffer;

    private final double conversion = 1./7. * 6 * Math.PI * 2; //Rotations to inches

    private boolean distanceCompleted;
    private boolean stopFlag;

    private final double distanceClose = 85; //inches
    private final double distanceFar = 178; //inches
    private final double band = 10;


    public LimelightDistanceCommand(Drivetrain subsystemA, VPLimelight subsystemB, boolean close){
        mDrivetrain = subsystemA;
        mVision = subsystemB;

        addRequirements(mVision);

        goalDistance = close ? distanceClose : distanceFar;

        mDrivetrain.mState = Constants.DriveTrain.DriveState.TELE_LIMELIGHT;
    }

    @Override
    public void initialize(){
        mDrivetrain.mState = Constants.DriveTrain.DriveState.TELE_LIMELIGHT;
        stopFlag = false;
        speed = 0.5;
        buffer = 2.5;
        mVision.updateTargets();
//        goalDistance = goalDistance; //inches

        initDistance = mVision.calcDistance();
        goalTravel = initDistance - goalDistance;
        mDrivetrain.resetEncoders();

//        if (goalTravel < 5 || goalTravel > -5) distanceCompleted = true;
//        else distanceCompleted = false;

        System.out.println("Goal distance (in): "+goalDistance);
        System.out.println("Initial distance (in): "+initDistance);
        System.out.println("Expected travel distance (in) "+goalTravel);

    }

    @Override
    public void execute(){

        actualTravel = Units.metersToInches(-mDrivetrain.rightWheelsPosition());
//        System.out.println(mRightEncoder.getPosition());
//        speed = calcSpeed();
        remainDistance = goalTravel-actualTravel;
        speed = 0;


        if (Math.abs(remainDistance) > buffer) {
            speed = -Math.signum(remainDistance)*MathEqs.targetLinear2(remainDistance, 0.65, 0.17,50, buffer);
//            speed = -Math.signum(remainDistance)*MathEqs.targetLinear(remainDistance, 0.6,100, buffer);
            System.out.println("SPEED: "+MathEqs.roundCustom(speed)+" | TRAVEL: " + MathEqs.roundCustom(actualTravel) +
                    " | REMAINING: "+MathEqs.roundCustom(remainDistance));
            System.out.println("Remaining Distance: "+remainDistance);
        }
        else {
            System.out.println("Please work");
            if (true) {
                System.out.println("STOPPING ALIGNMENT");
                stopFlag = true;
                end(true);
            }
        }

        mVision.setValues(speed, 0);

//        System.out.println(initDistance);
//        System.out.println(goalTravel - actualTravel);
    }


    @Override
    public boolean isFinished(){
        return stopFlag || distanceCompleted;
    }

    @Override
    public void end(boolean isFinished) {
        mVision.setValues(0,0);


        mDrivetrain.mState = Constants.DriveTrain.DriveState.TELE_DRIVE_INTAKE;

    }


}
