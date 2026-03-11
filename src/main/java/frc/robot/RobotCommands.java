package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.LauncherConstants;
import frc.robot.Constants.SweeperConstants;
import frc.robot.Configs.MAXSwerveModule;

public class RobotCommands { 
    // Intializes the intake Motor on the Robot as A SparkFlex Brushless Motor
    static SparkMax intakeMotor = new SparkMax(LauncherConstants.intakeMotor, MotorType.kBrushless);
    static RelativeEncoder intakeEncoder = intakeMotor.getEncoder();
    static SparkClosedLoopController intakePID = intakeMotor.getClosedLoopController();

    // Intializes the launcher Motor on the Robot as A SparkFlex Brushless Motor
    static SparkMax launcherMotor = new SparkMax(LauncherConstants.launcherMotor, MotorType.kBrushless);
    static RelativeEncoder launcherEncoder = launcherMotor.getEncoder();
    static SparkClosedLoopController launcherPID = launcherMotor.getClosedLoopController();

    // Intializes the feed Motor on the Robot as A SparkFlex Brushless Motor
    static SparkMax feedMotor = new SparkMax(LauncherConstants.feedMotor, MotorType.kBrushless);
    static RelativeEncoder feedEncoder = feedMotor.getEncoder();
    static SparkClosedLoopController feedPID = feedMotor.getClosedLoopController();

    // Configure PID for all motors
    static {
        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        SparkMaxConfig launcherConfig = new SparkMaxConfig();
        SparkMaxConfig feedConfig = new SparkMaxConfig();
        

        intakeConfig.closedLoop
            .p(0.0003)
            .i(0.0)
            .d(0.0)
            .velocityFF(0.00019)
            .outputRange(-1, 1);

        launcherConfig.closedLoop
            .p(0.0003)
            .i(0.0)
            .d(0.0)
            .velocityFF(0.00019)
            .outputRange(-1, 1);

        feedConfig.closedLoop
            .p(0.0003)
            .i(0.0)
            .d(0.0)
            .velocityFF(0.00016)
            .outputRange(-1, 1);

        // Apply to the motor
        intakeMotor.configure(intakeConfig, 
            SparkBase.ResetMode.kResetSafeParameters, 
            SparkBase.PersistMode.kPersistParameters);

        launcherMotor.configure(launcherConfig, 
            SparkBase.ResetMode.kResetSafeParameters, 
            SparkBase.PersistMode.kPersistParameters);

        feedMotor.configure(feedConfig, 
            SparkBase.ResetMode.kResetSafeParameters, 
            SparkBase.PersistMode.kPersistParameters);
    }
       
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

    public static double getLauncherRPM() {
        return launcherEncoder.getVelocity();
    }

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
                feedPID.setReference(-3000, SparkBase.ControlType.kVelocity);
            } else {
                feedPID.setReference(3000, SparkBase.ControlType.kVelocity);
            }
        } else {
            if (isIntakeForward) {
                feedPID.setReference(-LauncherConstants.kMaxRotationFeedSpeed, SparkBase.ControlType.kVelocity);
            } else {
                feedPID.setReference(LauncherConstants.kMaxRotationFeedSpeed, SparkBase.ControlType.kVelocity);
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
        intakePID.setReference(LauncherConstants.kMaxRotationIntakeSpeed, SparkBase.ControlType.kVelocity);
        feedPID.setReference(-LauncherConstants.kMaxRotationFeedSpeed, SparkBase.ControlType.kVelocity);
        launcherPID.setReference(LauncherConstants.kMaxRotationPickupSpeed, SparkBase.ControlType.kVelocity);
    }

    static void shooterFeedReduce() {
        feedPID.setReference(LauncherConstants.kMaxRotationFeedSpeed, SparkBase.ControlType.kVelocity);
    }

    static void shooterEverythingStart() {
        isIntakeForward = false;
        intakePID.setReference(-LauncherConstants.kMaxRotationIntakeSpeed,SparkBase.ControlType.kVelocity);
        feedPID.setReference(3000, SparkBase.ControlType.kVelocity);
    }

    static void shooterLauncherStart(double speed) {
        isIntakeForward = false;
        launcherPID.setReference(speed,SparkBase.ControlType.kVelocity);
    }
        
    // Stops ALL Motors except Drive Train Motor    
    public static void motorStop(){
        launcherMotor.stopMotor();
        intakeMotor.stopMotor();
        feedMotor.stopMotor();
    }
}
