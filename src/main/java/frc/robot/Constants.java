// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final Joystick driverController = new Joystick(0);
    public static final Joystick operatorController = new Joystick(1);

    public static final class DriveTrain {

        public static final int leftLeaderPort = 1;
        public static final int leftFollowerAPort = 2;
        public static final int leftFollowerBPort = 3;
        public static final int rightLeaderPort = 4;
        public static final int rightFollowerAPort = 5;
        public static final int rightFollowerBPort = 6;

        public static final int[] shifterPorts = {0, 1};

        public static final double deadband = 0.02;

    }

    public static final class Intake {

        public static final int intakeMotorPort = 7;
        public static final int intakeStaticMotorPort = 8;

        public static final int[] fourBarPorts = {2, 3};
    }

    public static final class Indexer {

        public static final int indexerPort = 11;
    }

    public static final class Shooter {

        public static final int shooterAPort = 9;
        public static final int shooterBPort = 10;

        public static final int[] anglerPorts = {4, 5};
    }
    public static final class Climber{

        public static final int climberPort = 11;
        public static final int[] solenoidPorts = {6, 7};
    }

}
