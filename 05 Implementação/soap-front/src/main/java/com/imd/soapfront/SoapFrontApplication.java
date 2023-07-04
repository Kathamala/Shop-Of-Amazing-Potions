package com.imd.soapfront;

import java.io.IOException;
import java.util.Scanner;

import com.imd.soapfront.helper.HttpRequestHandler;
import com.imd.soapfront.helper.ResultHelper;

public class SoapFrontApplication {

	final static String URL_BASE = "http://localhost:8080";
	final static String MAINGAME_CONTROLLER = "/soap/maingame";

	public static void main(String[] args) throws IOException {
		System.out.println("############ BEM VINDO AO SOAP ############");
		System.out.println("");
		int operation = -1;
		Scanner scanner = new Scanner(System.in);
		try {
			while (operation != 0) {
				System.out.println("1) Entrar no SOAP.");
				System.out.println("2) Criar conta.");
				System.out.println("");
				System.out.print("Informe a ação que deseja realizar: ");
				operation = scanner.nextInt();
				switch (operation) {
					case 0: {
						System.out.println("Exiting the Bank System");
						System.out.println("");
						break;
					}
					case 1: {
						System.out.println("Login");
						login(scanner);
						System.out.println("");
						break;
					}					
				}
			}
		} finally {
			scanner.close();
		}
	}

	private static void login(Scanner scanner) throws IOException {
		System.out.print("Digite o nome do jogador: ");
		String playerName = scanner.nextLine();
		
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/?nome=" + playerName, "POST", "");

		if (result.isStatus()) {
			System.out.println("Account created successfully");
			System.out.println("");
		} else {
			System.out.println("Failed to create account (" + result.getMessage() + ")");
			System.out.println("");
		}
	}	
}
