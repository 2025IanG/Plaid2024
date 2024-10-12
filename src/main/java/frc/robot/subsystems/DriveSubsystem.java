package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.ReplanningConfig;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;

import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {

    private final TalonFX leftMotor1;
    private final TalonFX leftMotor2;
    private final TalonFX rightMotor1;
    private final TalonFX rightMotor2;

    private final DifferentialDrive differentialDrive;

    private final TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    private final AHRS m_gyro = new AHRS(SPI.Port.kMXP);

    private boolean isBrakeOn = false;

    public final DifferentialDriveOdometry m_odometry;

    public final DifferentialDriveKinematics m_kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(22));
    
    public DriveSubsystem() {

        leftMotor1 = new TalonFX(DriveConstants.kLeftMotor1Port);
        leftMotor2 = new TalonFX(DriveConstants.kLeftMotor2Port);
        rightMotor1 = new TalonFX(DriveConstants.kRightMotor1Port);
        rightMotor2 = new TalonFX(DriveConstants.kRightMotor2Port);

        // Not sure if this works, use Phoenix Tuner if necessary
        talonConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0;

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

        m_odometry = new DifferentialDriveOdometry(
            Rotation2d.fromDegrees(m_gyro.getYaw()), 
            encoderTicksToMeters(leftMotor1.getPosition().getValueAsDouble()), 
            encoderTicksToMeters(rightMotor1.getPosition().getValueAsDouble()),
            new Pose2d()
        );

        AutoBuilder.configureRamsete(
            this::getPose, // Robot pose supplier
            this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getCurrentSpeeds, // Current ChassisSpeeds supplier
            this::drive, // Method that will drive the robot given ChassisSpeeds
            new ReplanningConfig(), // Default path replanning config. See the API for the options here
            () -> {
              // Boolean supplier that controls when the path will be mirrored for the red alliance
              // This will flip the path being followed to the red side of the field.
              // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this // Reference to this subsystem to set requirements
    );

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

    public double encoderTicksToMeters(double ticks) {
        return Units.inchesToMeters(ticks * (50/24) * (3*Math.PI));
    }

    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    public void resetPose(Pose2d pose) {
        m_odometry.resetPosition(Rotation2d.fromDegrees(m_gyro.getYaw()), 
            encoderTicksToMeters(leftMotor1.getPosition().getValueAsDouble()), 
            encoderTicksToMeters(rightMotor1.getPosition().getValueAsDouble()), 
            pose
        );
    }

    public ChassisSpeeds getCurrentSpeeds() {
        return m_kinematics.toChassisSpeeds(
            new DifferentialDriveWheelSpeeds(
                encoderTicksToMeters(leftMotor1.getVelocity().getValueAsDouble()), 
                encoderTicksToMeters(rightMotor1.getVelocity().getValueAsDouble())
            )
        );
    }

    public void drive(ChassisSpeeds speeds) {
        ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(speeds, 0.2);

        m_kinematics.toWheelSpeeds(targetSpeeds);
    }
}

