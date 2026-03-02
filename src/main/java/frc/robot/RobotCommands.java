package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;

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
    static DoubleSolenoid climbVertNoid = new DoubleSolenoid(54,PneumaticsModuleType.REVPH, 5, 8);
    // Creates a New Duouble solenoid value for the gun on the bot using Module 54, the REVPH module, with a forward and reversechannel pins
    static DoubleSolenoid boxNoid = new DoubleSolenoid(54,PneumaticsModuleType.REVPH,3, 10);
    
    // Variable Declaration that assists in the creation of if statements the name suggests what they relate to
    public static boolean isClimbUp = false;
    public static boolean isClimbOut = false;
    public static boolean isBoxOut = false;
    public static boolean isIntakeForward= false;

    private static double launcherSpeed = LauncherConstants.kMaxRotationLauncherSpeed;

    //A function that sets all Pneumatics 
    public static void setStartPneumatics(){
        climbHorizNoid.set(DoubleSolenoid.Value.kReverse);
        climbVertNoid.set(DoubleSolenoid.Value.kReverse);
        boxNoid.set(DoubleSolenoid.Value.kReverse);
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
        if(isClimbUp==true) {
            climbVertNoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            climbVertNoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    // A function to either set the boxNoid pneumatic in or out using the IsSweeperOut value and the .set functionality to tell the solenoid what to do.
    public static void boxInOrOut() {
        isBoxOut = !isBoxOut;
        if(isClimbOut==true) {
            boxNoid.set(DoubleSolenoid.Value.kForward);
        }
        else {
            boxNoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    static void intakeForwardOrReverse() {
        isIntakeForward = !isIntakeForward;
        launcherForward();
        if(isIntakeForward==true) {
            intakeMotor.set(launcherSpeed);
            feedMotor.set(-LauncherConstants.kMaxRotationFeedSpeed);
        }
        else {
            intakeMotor.set(-launcherSpeed);
            feedMotor.set(LauncherConstants.kMaxRotationFeedSpeed);
        }
    }

    // Sets the launcher motor to spin in the neccasary directions and the proper speed. 
    public static void launcherForward(){
        launcherMotor.set(launcherSpeed);
    } 
        
    // Stops ALL Motors except Drive Train Motor    
    public static void motorStop(){
        launcherMotor.stopMotor();
        intakeMotor.stopMotor();
        sweeperMotor.stopMotor();   
        isIntakeForward = true;
    }
}
