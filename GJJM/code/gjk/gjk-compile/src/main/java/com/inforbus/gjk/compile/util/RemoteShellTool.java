package com.inforbus.gjk.compile.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteShellTool {
//	public static void main(String[] args) throws Exception{
//		Connection connection = new Connection("192.168.234.128");
//		connection.connect();
//		connection.authenticateWithPassword("root1", "root");
//		Session session = connection.openSession();
//		session.execCommand("cd /gjk/SIGProject/MIPSI64diable_SMP ; ls");
//		InputStream is = new StreamGobbler(session.getStdout());
//		BufferedReader brs = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
//		String temp = "";
//		while ((temp = brs.readLine()) != null) {
//			System.out.println(temp);
//		}
//		
//		InputStream is1 = new StreamGobbler(session.getStderr());
//		BufferedReader brs1 = new BufferedReader(new InputStreamReader(is1,Charset.forName("UTF-8")));
//		String temp1 = "";
//		while ((temp1 = brs1.readLine()) != null) {
//			System.out.println(temp1);
//		}
//		if(session != null) {
//			session.close();
//		}
//		session.close();
//		brs.close();
//	}
}
