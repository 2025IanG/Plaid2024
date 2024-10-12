package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

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
  public final PusherSubsystem m_pusherSubsystem = new PusherSubsystem();
  public final DoorSubsystem m_doorSubsystem = new DoorSubsystem();

  private final XboxController driveController = new XboxController(0);
  private final JoystickButton driveStart = new JoystickButton(driveController, XboxController.Button.kStart.value);
  private final JoystickButton driveBack = new JoystickButton(driveController, XboxController.Button.kBack.value);
  private final JoystickButton driveA = new JoystickButton(driveController, XboxController.Button.kA.value);
  private final JoystickButton driveB = new JoystickButton(driveController, XboxController.Button.kB.value);
  private final JoystickButton driveX = new JoystickButton(driveController, XboxController.Button.kX.value);
  private final JoystickButton driveY = new JoystickButton(driveController, XboxController.Button.kY.value);

  private final SendableChooser<String> autoChooser = new SendableChooser<String>();

  /**
  * The container for the robot.  Contains subsystems, OI devices, and commands.
  */
  private RobotContainer() {

    SmartDashboard.putData("DefaultDriveCommand", new DefaultDriveCommand(m_driveSubsystem, driveController));

    m_driveSubsystem.setDefaultCommand(new DefaultDriveCommand(m_driveSubsystem, driveController));

    configureButtonBindings();

    setUpDriveTab();

    NamedCommands.registerCommand("Pusher Forward", new PusherForward(m_pusherSubsystem));
    NamedCommands.registerCommand("Door Open", new DoorUp(m_doorSubsystem));
    NamedCommands.registerCommand("Door Closed", new DoorDown(m_doorSubsystem) );
    NamedCommands.registerCommand("Pusher Back", new PusherBackward(m_pusherSubsystem));
    NamedCommands.registerCommand("Door Sequence", new DoorSequence(m_pusherSubsystem, m_doorSubsystem));

    autoChooser.setDefaultOption("Amp and Middle", "A&M");
    autoChooser.addOption("Amp to Destruction", "ATD");
    autoChooser.addOption("Out of Way Source", "OWS");
    autoChooser.addOption("Out of Way to Destruction", "OWD");
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

    driveBack.onTrue(
      new InstantCommand(() -> {m_driveSubsystem.setBrakeMode();}, m_driveSubsystem)
    ).onFalse(
      new InstantCommand(() -> {m_driveSubsystem.setCoastMode();}, m_driveSubsystem)
    );

    driveStart.onTrue(
      new DoorSequence(m_pusherSubsystem, m_doorSubsystem)
    );

    driveA.whileTrue(
      new PusherForward(m_pusherSubsystem)
    );

    driveB.whileTrue(
      new PusherBackward(m_pusherSubsystem)
    );

    driveX.whileTrue(
      new DoorDown(m_doorSubsystem)
    );

    driveY.whileTrue(
      new DoorUp(m_doorSubsystem)
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
    
    switch(autoChooser.getSelected()) {
      case "A&M":
        return AutoBuilder.buildAuto("Amp and Middle");
      case "ATD":
        return AutoBuilder.buildAuto("Amp to Destruction");
      case "OWS":
        return AutoBuilder.buildAuto("Out of Way SS");
      case "OWD":
        return AutoBuilder.buildAuto("Out of Way to Destruction");
      default:
        return AutoBuilder.buildAuto("Amp and Middle");      
    }
  }

  public void setUpDriveTab() {
    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive Tab");

    driveTab.add(autoChooser)
      .withSize(2, 1)
      .withPosition(0, 0);

    driveTab.addBoolean("Is Brake On",
      () -> {
        return m_driveSubsystem.isBrakeOn();
      }).withSize(1, 1)
      .withPosition(1, 0)
      .withWidget(BuiltInWidgets.kBooleanBox);
  }

}

