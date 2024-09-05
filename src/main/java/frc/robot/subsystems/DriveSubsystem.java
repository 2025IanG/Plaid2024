package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {

    private final TalonFX leftMotor1;
    private final TalonFX leftMotor2;
    private final TalonFX rightMotor1;
    private final TalonFX rightMotor2;

    private final DifferentialDrive differentialDrive;

    private final TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    private boolean isBrakeOn = false;

    public final CANSparkMax wahoo = new CANSparkMax(37, MotorType.kBrushless);
    
    public DriveSubsystem() {

        leftMotor1 = new TalonFX(DriveConstants.kLeftMotor1Port);
        leftMotor2 = new TalonFX(DriveConstants.kLeftMotor2Port);
        rightMotor1 = new TalonFX(DriveConstants.kRightMotor1Port);
        rightMotor2 = new TalonFX(DriveConstants.kRightMotor2Port);

        // Not sure if this works, use Phoenix Tuner if necessary
        talonConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 1;

        leftMotor1.getConfigurator().apply(talonConfig);
        leftMotor2.getConfigurator().apply(talonConfig);
        rightMotor1.getConfigurator().apply(talonConfig);
        rightMotor2.getConfigurator().apply(talonConfig);

        leftMotor1.setInverted(true);
        leftMotor2.setInverted(true);
        rightMotor1.setInverted(false);
        rightMotor2.setInverted(false);

        leftMotor1.setNeutralMode(NeutralModeValue.Coast);
        leftMotor2.setNeutralMode(NeutralModeValue.Coast);
        rightMotor1.setNeutralMode(NeutralModeValue.Coast);
        rightMotor2.setNeutralMode(NeutralModeValue.Coast);

        leftMotor2.setControl(new Follower(DriveConstants.kLeftMotor1Port, false));
        rightMotor2.setControl(new Follower(DriveConstants.kRightMotor1Port, false));

        differentialDrive = new DifferentialDrive(leftMotor1, rightMotor1);

    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}

    public void setLeftMotors(double speed) {
        leftMotor1.set(speed);
        leftMotor2.set(speed);
    }

    public void stopLeftMotors() {
        leftMotor1.stopMotor();
        leftMotor2.stopMotor();
    }

    public void setRightMotors(double speed) {
        rightMotor1.set(speed);
        rightMotor2.set(speed);
    }

    public void stopRightMotors() {
        rightMotor1.stopMotor();
        rightMotor2.stopMotor();
    }

    public void setAllMotors(double speed) {
        setLeftMotors(speed);
        setRightMotors(speed);
    }

    public void stopAllMotors() {
        stopLeftMotors();
        stopRightMotors();
    }

    public void arcadeDrive(double forward, double rotation) {
        differentialDrive.arcadeDrive(forward, rotation);
    }

    public void setCoastMode() {
        leftMotor1.setNeutralMode(NeutralModeValue.Coast);
        leftMotor2.setNeutralMode(NeutralModeValue.Coast);
        rightMotor1.setNeutralMode(NeutralModeValue.Coast);
        rightMotor2.setNeutralMode(NeutralModeValue.Coast);

        leftMotor2.setControl(new Follower(DriveConstants.kLeftMotor1Port, false));
        rightMotor2.setControl(new Follower(DriveConstants.kRightMotor1Port, false));

        isBrakeOn = false;
    }

    public void setBrakeMode() {
        leftMotor1.setNeutralMode(NeutralModeValue.Brake);
        leftMotor2.setNeutralMode(NeutralModeValue.Brake);
        rightMotor1.setNeutralMode(NeutralModeValue.Brake);
        rightMotor2.setNeutralMode(NeutralModeValue.Brake);

        leftMotor2.setControl(new Follower(DriveConstants.kLeftMotor1Port, false));
        rightMotor2.setControl(new Follower(DriveConstants.kRightMotor1Port, false));

        isBrakeOn = true;
    }

    public boolean isBrakeOn() {
        return isBrakeOn;
    }
}

