/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//test add

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

//currently unused

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  TalonFX FXtest = new TalonFX(15);

  TalonFX FrontLeft = new TalonFX(16);
  TalonFX FrontRight = new TalonFX(15);
  TalonFX BackLeft = new TalonFX(17);
  TalonFX BackRight = new TalonFX(18);

  WPI_TalonFX m_FrontLeft = new WPI_TalonFX(16);
  WPI_TalonFX m_FrontRight = new WPI_TalonFX(15);
  
  WPI_TalonFX m_BackLeft = new WPI_TalonFX(17);
  

  WPI_TalonFX m_BackRight = new WPI_TalonFX(18);


  private final Joystick m_stick = new Joystick(0);
  Joystick m_buttons = new Joystick(0);

  final JoystickButton k3 = new JoystickButton(m_buttons, 10);
  PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();


  double FL = 0;
  double FR = 0;
  double BL = 0;
  double BR = 0;

  public double Y = .1;
  public double X = 0;
  public double Z = 0;

  int compr;

  int button;

  DoubleSolenoid doubleSolenoid = new DoubleSolenoid(0,1);

  int FLencoder;

  

  //Solenoid num1 = new Solenoid(0);
  //Solenoid num2 = new Solenoid(1);

  Compressor compressor = new Compressor(0);
  //boolean enabled = compressor.enabled();
  //boolean pressureSwich = compressor.getPressureSwitchValue();



  /*


  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput * .8);
  }
  */


  private final DifferentialDrive m_drive = new DifferentialDrive(m_FrontLeft,m_FrontRight);


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    //I am also going to use this as a sort of constants place
    
    FrontLeft.setNeutralMode(NeutralMode.Brake);
    FrontRight.setNeutralMode(NeutralMode.Brake);
    BackRight.setNeutralMode(NeutralMode.Brake);
    BackLeft.setNeutralMode(NeutralMode.Brake);
    
    m_BackLeft.follow(m_FrontLeft);
  public void robotPeriodic() {
    //k3.whenPressed(new drive());
    
    
    
    /*System.out.println(POwa);
    FXtest.set(ControlMode.PercentOutput,POwa);
    POwa = POwa - 1;
    */
    

    
  }

  @Override
  public void autonomousInit() {
    /*for (int i = 1; i <= 100; i++) {
    
    final int Speed = 100 - i;
    System.out.println(i);
    FXtest.set(ControlMode.PercentOutput,Speed);
    }
    */
    //doubleSolenoid.set(Value.kOff);
    //num1.set(true);
    //num2.set(false);
    FrontLeft.set
    



  }

  @Override
  public void autonomousPeriodic() {
    System.out.println(FrontLeft.getSelectedSensorPosition());
    //while (Fwd > 1200);

    
  }

  @Override
  public void teleopInit() {
    button = 1;
    compr = 1;

  }

  @Override
  public void teleopPeriodic() {
    if (m_stick.getRawButton(4)){
      button = -1;
    }
    if (m_stick.getRawButton(3)){
      button = 1;
    }
    if (m_stick.getRawButton(10)){
      compr = -1;
    }
    if (m_stick.getRawButton(5)){
      compr = 1;
    }
    if (compr == 1){
      compressor.start();
    }
    else{
      compressor.stop();
    }

    X = m_stick.getX();
    Y = m_stick.getY();
    Z = m_stick.getZ();
    
    if (Math.abs(X) <= .1){
      X = 0;
    }
    if (Math.abs(Y) <= .1){
      Y = 0;
    }
    if (Math.abs(Z) <= .1){
      Z = 0;
    }
    //System.out.println(X);
    //System.out.println(Y);
    //System.out.println(Z);
    Z = -Z;
    X = -X;

    

    
    if (m_stick.getRawButton(1)){
      X = X * 2;
      Y = Y * 2;
      Z = Z * 1.25;
    }
    else{
      X = X*.75;
      Y = Y*.75;
      Z = Z*.5;
    }

    //while (m_stick.getRawButton(4)){
      //button = -button;
    //}
    if (button == -1){
      doubleSolenoid.set(Value.kReverse);
      

      FL = Y+(X+Z);
      FR = Y-(X+Z);
      BL = Y-(X-Z);
      BR = Y+(X-Z);

      //reverses the RIGHT SIDE
      FR = -FR;
      BR = -BR;
    
      FrontLeft.set(ControlMode.PercentOutput,FL);
      FrontRight.set(ControlMode.PercentOutput,FR);
      BackLeft.set(ControlMode.PercentOutput,BL);
      BackRight.set(ControlMode.PercentOutput,BR);
    }
    else {
      doubleSolenoid.set(Value.kForward);

      m_drive.arcadeDrive(Y, Z);
      m_BackLeft.follow(m_FrontLeft);
      m_BackRight.follow(m_FrontRight);
    }


  }

  @Override
  public void disabledInit() {
    doubleSolenoid.set(Value.kOff);
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
    
    
    
  }

  @Override
  public void testPeriodic() {
    doubleSolenoid.set(Value.kForward);

    Z = m_stick.getZ() * -1;
    m_drive.arcadeDrive(m_stick.getY(), Z);
    m_BackLeft.follow(m_FrontLeft);
    m_BackRight.follow(m_FrontRight);

  }

}
