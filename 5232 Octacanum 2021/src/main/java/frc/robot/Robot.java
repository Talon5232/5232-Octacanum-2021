/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;


//currently unused

import edu.wpi.first.wpilibj.Timer;
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
  TalonFX FrontRight = new TalonFX(16);
  TalonFX BackLeft = new TalonFX(16);
  TalonFX BackRight = new TalonFX(16);

  WPI_TalonFX m_FrontLeft = new WPI_TalonFX(16);
  WPI_TalonFX m_FrontRight = new WPI_TalonFX(16);
  
  WPI_TalonFX m_BackLeft = new WPI_TalonFX(16);
  

  WPI_TalonFX m_BackRight = new WPI_TalonFX(16);


  private final Joystick m_stick = new Joystick(0);


  double FL = 0;
  double FR = 0;
  double BL = 0;
  double BR = 0;

  public double Y = .1;
  public double X = 0;
  public double Z = 0;

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
    m_BackRight.follow(m_FrontRight);

    
    

    System.out.println("-----------------Start of the program-----------------");
    //SRXtets.set(ControlMode.PercentOutput,50);
    FXtest.set(ControlMode.PercentOutput,0);

    //FrontLeft.set(ControlMode.PercentOutput,0);

  }

  @Override
  public void robotPeriodic() {
    
    
    /*System.out.println(POwa);
    FXtest.set(ControlMode.PercentOutput,POwa);
    POwa = POwa - 1;
    */

    
  }

  @Override
  public void autonomousInit() {
    for (int i = 1; i <= 100; i++) {
    
    final int Speed = 100 - i;
    System.out.println(i);
    FXtest.set(ControlMode.PercentOutput,Speed);
    }


  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
     
  }

  @Override
  public void teleopPeriodic() {
    X = m_stick.getX();
    Y = m_stick.getY();
    Z = m_stick.getZ();
    FL = Y+(X+Z);
    FR = Y-(X+Z);
    BL = Y-(X-Z);
    BR = Y+(X-Z);
    
    System.out.println("Stick X");
    System.out.print(X);
    System.out.print(" Y");
    System.out.print(Y);
    System.out.print(" Z");
    System.out.print(Z);
    System.out.print(" | Motor FL");
    System.out.print(FL);
    System.out.print(" FR");
    System.out.print(FR);
    System.out.print(" BL");
    System.out.print(BL);
    System.out.print(" BR");
    System.out.print(BR);

    FrontLeft.set(ControlMode.PercentOutput,FL);
    FrontRight.set(ControlMode.PercentOutput,FR);
    BackLeft.set(ControlMode.PercentOutput,BL);
    BackRight.set(ControlMode.PercentOutput,BR);


  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
    

    
    
  
    
  }

  @Override
  public void testPeriodic() {
    m_drive.arcadeDrive(m_stick.getY(), m_stick.getX());
    
    
    
    
    
    //for (int i = 1; i <= 100; i++) {
    //  FrontLeft.set(ControlMode.PercentOutput,i);
    //  FrontRight.set(ControlMode.PercentOutput,i);

    //}

    /////////////////////////////m_drive.arcadeDrive(m_stick.getY(), m_stick.getX());
    
    //joystick part
    /*

    this is old code!!

    double TankRight = -Y - X;
    double TankLeft =  -Y + X;
    
    
    FrontLeft.set(ControlMode.PercentOutput, TankLeft);
    FrontRight.set(ControlMode.PercentOutput,TankRight);
    */


  }

}
