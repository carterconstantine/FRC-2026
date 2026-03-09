package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import com.revrobotics.spark.SparkFlex;
import frc.robot.Constants.LauncherConstants;
import frc.robot.Constants.SweeperConstants;

public class RobotCommands { 
    // Intializes the intake Motor on the Robot as A SparkFlex Brushless Motor
    static SparkFlex intakeMotor = new SparkFlex(LauncherConstants.intakeMotor, MotorType.kBrushless);
    // Intializes the launcher Motor on the Robot as A SparkFlex Brushless Motor
    static SparkFlex launcherMotor = new SparkFlex(LauncherConstants.launcherMotor, MotorType.kBrushless);
    // Intializes the feed Motor on the Robot as A SparkFlex Brushless Motor
    static SparkFlex feedMotor = new SparkFlex(LauncherConstants.feedMotor, MotorType.kBrushless);
    // Intializes the sweeperMotor Motor on the Robot as A SparkFlex Brushless Motor
    static SparkFlex sweeperMotor = new SparkFlex(SweeperConstants.sweeperMotor, MotorType.kBrushless);
       
    // Creates a New Duouble solenoid value for the Algae Pusher on the bot using Module 54, the REVPH module, with a forward and reversechannel pins
    static DoubleSolenoid climbHorizNoid = new DoubleSolenoid(54,PneumaticsModuleType.REVPH,1,12);
    // Creates a New Duouble solenoid value for the Elevator on the bot using Module 54, the REVPH module, with a forward and reversechannel pins
    static DoubleSolenoid climbVertNoid = new DoubleSolenoid(54,PneumaticsModuleType.REVPH,5,8);
    // Creates a New Duouble solenoid value for the gun on the bot using Module 54, the REVPH module, with a forward and reversechannel pins
    static DoubleSolenoid boxNoid = new DoubleSolenoid(54,PneumaticsModuleType.REVPH,3, 10);
    
    static DigitalInput limitSwitch = new DigitalInput(0);

    // Variable Declaration that assists in the creation of if statements the name suggests what they relate to
    public static boolean isClimbUp = false;
    public static boolean isClimbOut = false;
    public static boolean isBoxOut = false;
    public static boolean isIntakeForward = false;
    public static boolean isFeedTurbo = false;

    private static double launcherSpeed = LauncherConstants.kMaxRotationLauncherSpeed;

    public static boolean limitSwitchPressed() {
        return !limitSwitch.get();
    }

    //A function that sets all Pneumatics 
    public static void setStartPneumatics(){
        climbHorizNoid.set(DoubleSolenoid.Value.kReverse);
        climbVertNoid.set(DoubleSolenoid.Value.kReverse);
        boxNoid.set(DoubleSolenoid.Value.kReverse);
    }

    public static void feedTurboOnOrOff() {
        isFeedTurbo = !isFeedTurbo;
        if (isFeedTurbo) {
            if (isIntakeForward) {
                feedMotor.set(-0.40);
            } else {
                feedMotor.set(0.40);
            }
        } else {
            if (isIntakeForward) {
                feedMotor.set(-LauncherConstants.kMaxRotationFeedSpeed);
            } else {
                feedMotor.set(LauncherConstants.kMaxRotationFeedSpeed);
            }
        }
    }

    public static void climbUp() {
        isClimbUp = true;
        climbVertNoid.set(DoubleSolenoid.Value.kForward);
    }

    public static void climbDown() {
        isClimbUp = false;
        climbVertNoid.set(DoubleSolenoid.Value.kReverse);
    }

    public static void climbOut() {
        isClimbOut = true;
        climbHorizNoid.set(DoubleSolenoid.Value.kForward);
    }

    public static void climbIn() {
        isClimbOut = false;
        climbHorizNoid.set(DoubleSolenoid.Value.kReverse);
    }

    // A function to either set the climbVertNoid pneumatic up or down using the IsSweeperOut value and the .set functionality to tell the solenoid what to do.
    public static void climbUpOrDown() {
        isClimbUp = !isClimbUp;
        if(isClimbUp==true) {
            climbVertNoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            climbVertNoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    // A function to either set the climbVertNoid pneumatic up or down using the IsSweeperOut value and the .set functionality to tell the solenoid what to do.
    public static void climbInOrOut() {
        isClimbOut = !isClimbOut;
        if(isClimbOut==true) {
            climbHorizNoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            climbHorizNoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    // A function to either set the boxNoid pneumatic in or out using the IsSweeperOut value and the .set functionality to tell the solenoid what to do.
    public static void boxInOrOut() {
        isBoxOut = !isBoxOut;
        if(isBoxOut==true) {
            boxNoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            boxNoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    static void intakeMode() {
        isIntakeForward = true;
        intakeMotor.set(LauncherConstants.kMaxRotationIntakeSpeed);
        feedMotor.set(-LauncherConstants.kMaxRotationFeedSpeed);
        launcherMotor.set(LauncherConstants.kMaxRotationPickupSpeed);
    }

    static void shooterMode() {
        isIntakeForward = false;
        intakeMotor.set(-LauncherConstants.kMaxRotationIntakeSpeed);
        feedMotor.set(LauncherConstants.kMaxRotationFeedSpeed);
        launcherMotor.set(launcherSpeed);
    }

    static void setLauncherSpeed(double newSpeed) {
        launcherSpeed = newSpeed;
        launcherMotor.set(launcherSpeed);
    }

    static void reverseLauncher() {
        launcherMotor.set(0-launcherSpeed);
    }
        
    // Stops ALL Motors except Drive Train Motor    
    public static void motorStop(){
        launcherMotor.stopMotor();
        intakeMotor.stopMotor();
        feedMotor.stopMotor();
    }
}
