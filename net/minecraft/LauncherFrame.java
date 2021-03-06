﻿package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class LauncherFrame extends Frame
{
	public static String calculateHash(MessageDigest algorithm,
            String fileName) throws Exception{
        FileInputStream    fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        @SuppressWarnings("resource")
		DigestInputStream  dis = new DigestInputStream(bis, algorithm);
 
        while (dis.read() != -1);
              byte[] hash = algorithm.digest();
 
        return byteArray2Hex(hash);
    }
private static String byteArray2Hex(byte[] hash) {
        @SuppressWarnings("resource")
		Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
 
	
  public static final int VERSION = 13;
  private static final long serialVersionUID = 1L;
  public Map<String, String> customParameters = new HashMap<String, String>();
  public Launcher launcher;
  public LoginForm loginForm;

  public LauncherFrame()
  {
	 
	
    super(Config.title);

    setBackground(Color.BLACK);
    loginForm = new LoginForm(this);
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(loginForm, "Center");

    p.setPreferredSize(new Dimension(854, 480));

    setLayout(new BorderLayout());
    add(p, "Center");

    pack();
    setLocationRelativeTo(null);
    try
    {
      setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent arg0) {
        new Thread() {
          public void run() {
            try {
              Thread.sleep(30000L);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            System.out.println("FORCING EXIT!");
            System.exit(0);
          }
        }
        .start();
        if (launcher != null) {
          launcher.stop();
          launcher.destroy();
        }
        System.exit(0);
      } } );
  }

  public void playCached(String userName) {
    try {
      if ((userName == null) || (userName.length() <= 0)) {
        userName = "Player";
      }
      launcher = new Launcher();
      launcher.customParameters.putAll(customParameters);
      launcher.customParameters.put("userName", userName);
      launcher.init(); 
      removeAll();
      add(launcher, "Center");
      validate();
      launcher.start();
      loginForm = null;
      setTitle("Minecraft");
    } catch (Exception e) {
      e.printStackTrace();
      showError(e.toString());
    }
  }

//--------------------------------
//  public String getFakeResult(String userName) {
//	    return Util.getFakeLatestVersion() + ":35b9fd01865fda9d70b157e244cf801c:" + userName + ":12345:";
//	  }
//---------------------------------

  public void login(String userName, String password) {
    try {
      String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + (Config.version);
      String result = Util.excutePost(Config.auth, parameters);
//      String result = getFakeResult(userName);
      if (result == null) {
        showError("Невозможно подключится к серверу!");
        loginForm.setNoNetwork();
        return;
      }
      String applicationData = System.getenv("APPDATA");
      String  f = applicationData + Config.md51client;
 
      try
{
 
  MessageDigest md5  = MessageDigest.getInstance("MD5");
String p = calculateHash(md5, f);
 
 
                URL localURL = new URL(Config.md5server + p);
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
        String str2 = localBufferedReader.readLine();
        if (str2.equalsIgnoreCase("NO")){
            {
                GameUpdater.forceUpdate = true;
            }
 
      }
        else if (str2.equalsIgnoreCase("YES"))
        {
        }
 
  }
 
      catch(FileNotFoundException fnfn)
      {
      GameUpdater.forceUpdate = true; 
      }
 
 
 
      
      if (!result.contains(":")) {
        if (result.trim().equals("Bad login")) {
          showError("Неправильный логин или пароль!");
        } else if (result.trim().equals("Старая версия лаунчера!")) {
          loginForm.setOutdated();
          showError("Нужно обновить лаунчер!");
        } else {
          showError(result);
        }
        loginForm.setNoNetwork();
        return;
      }
      String[] values = result.split(":");

      launcher = new Launcher();
      launcher.customParameters.putAll(customParameters);
      launcher.customParameters.put("userName", values[2].trim());
      launcher.customParameters.put("latestVersion", values[0].trim());
      launcher.customParameters.put("downloadTicket", values[1].trim());
      launcher.customParameters.put("sessionId", values[3].trim());
      launcher.init();
      
      if (LoginForm.server == "1") {
          launcher.customParameters.put("server", Config.serverip1);
          launcher.customParameters.put("port", Config.serverport1);}
          launcher.init();
          
      if (LoginForm.server == "2") {
          launcher.customParameters.put("server", Config.serverip2);
          launcher.customParameters.put("port", Config.serverport2);}
          launcher.init();
          
      if (LoginForm.server == "3") {
          launcher.customParameters.put("server", Config.serverip3);
          launcher.customParameters.put("port", Config.serverport3);}
          launcher.init();
      if (LoginForm.server == "4") {
          launcher.customParameters.put("server", Config.serverip4);
          launcher.customParameters.put("port", Config.serverport4);}
          launcher.init();
      if (LoginForm.server == "5") {
          launcher.customParameters.put("server", Config.serverip5);
          launcher.customParameters.put("port", Config.serverport5);}
          launcher.init();
      if (LoginForm.server == "6") {
          launcher.customParameters.put("server", Config.serverip6);
          launcher.customParameters.put("port", Config.serverport6);}
          launcher.init();
              


      removeAll();
      add(launcher, "Center");
      validate();
      launcher.start();
      loginForm.loginOk();
      loginForm = null;
      setTitle("Minecraft");
    } catch (Exception e) {
      e.printStackTrace();
      showError(e.toString());
      loginForm.setNoNetwork();
    }
  }

  private void showError(String error) {
    removeAll();
    add(loginForm);
    loginForm.setError(error);
    validate();
  }

  public boolean canPlayOffline(String userName) {
    Launcher launcher = new Launcher();
    launcher.customParameters.putAll(customParameters);
    launcher.init(userName, null, null, null);
    return launcher.canPlayOffline();
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception localException) {
    }
    LauncherFrame launcherFrame = new LauncherFrame();
    launcherFrame.setVisible(true);
    launcherFrame.customParameters.put("stand-alone", "true");

    if (args.length >= 3) {
      String ip = args[2];
      String port = "25565";
      if (ip.contains(":")) {
        String[] parts = ip.split(":");
        ip = parts[0];
        port = parts[1];
      }

      launcherFrame.customParameters.put("server", ip);
      launcherFrame.customParameters.put("port", port);
    }

    if (args.length >= 1) {
      launcherFrame.loginForm.userName.setText(args[0]);
      if (args.length >= 2) {
        launcherFrame.loginForm.password.setText(args[1]);
        launcherFrame.loginForm.doLogin();
      }
    }
  }
}