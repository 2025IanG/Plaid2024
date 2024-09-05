package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;


/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();
  public final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

  private final XboxController driveController = new XboxController(0);

  private final JoystickButton driveStart = new JoystickButton(driveController, XboxController.Button.kStart.value);

  /**
  * The container for the robot.  Contains subsystems, OI devices, and commands.
  */
  private RobotContainer() {

    SmartDashboard.putData("DefaultDriveCommand", new DefaultDriveCommand(m_driveSubsystem, driveController));

    m_driveSubsystem.setDefaultCommand(new DefaultDriveCommand(m_driveSubsystem, driveController));

    configureButtonBindings();

    setUpDriveTab();
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    driveStart.onTrue(
      new InstantCommand(() -> {m_driveSubsystem.setBrakeMode();}, m_driveSubsystem)
    ).onFalse(
      new InstantCommand(() -> {m_driveSubsystem.setCoastMode();}, m_driveSubsystem)
    );
    
  }

  public XboxController getdriveController() {
    return driveController;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
  */
  public Command getAutonomousCommand() {
    return AutoPaths.doNothing();
  }

  public void setUpDriveTab() {
    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive Tab");

    driveTab.addBoolean("Is Brake On",
      () -> {
        return m_driveSubsystem.isBrakeOn();
      }).withSize(10, 5)
      .withPosition(0, 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

}

