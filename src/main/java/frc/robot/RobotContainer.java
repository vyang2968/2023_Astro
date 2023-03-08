package frc.robot;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;

import csplib.inputs.CSP_Controller;
import csplib.inputs.CSP_Controller.Scale;
import csplib.utils.AutoBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.arm.SetHigh;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.drivetrain.Drivetrain;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private CSP_Controller pilot = new CSP_Controller(Constants.controller.PILOT_PORT);
  private CSP_Controller copilot = new CSP_Controller(Constants.controller.COPILOT_PORT);

  private Drivetrain drivetrain = Drivetrain.getInstance();
  private Arm arm = Arm.getInstance();
  private Claw claw = Claw.getInstance();

  private SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Set the default commands
    setDefaultCommands();
    // Configure the button bindings
    configureButtonBindings();

    smartdashboardButtons();
    // Add options to the auto chooser
    addChooser();
  }

  private void setDefaultCommands() {
    drivetrain.setDefaultCommand(
        new RunCommand(
            () ->
                drivetrain.drive(
                    pilot.getLeftY(Scale.SQUARED),
                    pilot.getLeftX(Scale.SQUARED),
                    pilot.getRightX(Scale.SQUARED)),
            drivetrain));
  }

  /** Use this method to define your button->command mappings. */
  private void configureButtonBindings() {
    copilot.getAButton().whileTrue(new SetHigh(() -> copilot.getRightBumperButton().getAsBoolean()));
  }

  private void smartdashboardButtons() {
    SmartDashboard.putData(
        "Set Velocity",
        new RunCommand(
            () -> drivetrain.setVelocity(SmartDashboard.getNumber("Set Drive Velocity", 0)),
            drivetrain));

    SmartDashboard.putData(
        "Set Zero", new InstantCommand(() -> drivetrain.zeroPower(), drivetrain));
  }

  private void addChooser() {
    autoChooser.setDefaultOption("Do nothing", new SequentialCommandGroup());
    autoChooser.addOption(
        "Test", AutoBuilder.buildAuto("New Path", new HashMap<>(), new PathConstraints(5.0, 1)));
    autoChooser.addOption(
        "B21", AutoBuilder.buildAuto("B21", new HashMap<>(), new PathConstraints(5.0, 2)));

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
