package com.imd.soapfront;

import java.io.IOException;
import java.util.Scanner;

import com.imd.soapfront.helper.HttpRequestHandler;
import com.imd.soapfront.helper.ResultHelper;
import com.imd.soapfront.model.Jogador;
import com.imd.soapfront.model.MenuState;

public class SoapFrontApplication {

	final static String URL_BASE = "http://localhost:8080";
	final static String MAINGAME_CONTROLLER = "/soap/maingame";
	final static String DIVIDER = "######################################################\n";

	private static Jogador jogador;
	private static MenuState menu = MenuState.LOGIN;

	public static void main(String[] args) throws IOException {
		int operation = -1;
		Scanner scanner = new Scanner(System.in);
		try {
			//LOGIN
			while (operation != 0 & menu == MenuState.LOGIN) {
				System.out.println(DIVIDER);
				System.out.println("\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
				System.out.println("0) Sair do jogo");
				System.out.println("1) Entrar no jogo");
				System.out.println("2) Cadastrar jogador\n");
				System.out.println(DIVIDER);
				System.out.print("Informe a ação que deseja realizar: ");
				operation = scanner.nextInt();
				clearConsole();
				switch (operation) {
					case 0: {
						System.out.println("Encerrando o SOAP\n");
						break;
					}
					case 1: {
						login(scanner);
						System.out.println("");
						break;
					}
					case 2: {
						register(scanner);
						System.out.println("");
						break;
					}
				}
			}

			//MAIN GAME
			while (operation != 0) {
				if(menu == MenuState.MAIN){
					System.out.println(DIVIDER);
					System.out.println("\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
					System.out.println("\t\t     MENU PRINCIPAL     \t\t\n");	
					System.out.println("0) Sair do jogo");
					System.out.println("1) Visualizar Inventário");
					System.out.println("2) Clientes em Atendimento\n");
					System.out.println("3) Abrir a Loja\n");
					System.out.println("4) Fabricar Poção\n");
					System.out.println(DIVIDER);
				} else if(menu == MenuState.NPCS){
					System.out.println(DIVIDER);
					System.out.println("\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
					System.out.println("\t\t   CLIENTES EM ATENDIMENTO   \t\t\n");
					npcsAtendimento();
					System.out.println("1) Vender Poção");
					System.out.println("2) Voltar ao Menu Principal\n");
					System.out.println(DIVIDER);
				} else if(menu == MenuState.SHOP){
					System.out.println(DIVIDER);
					System.out.println("\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
					System.out.println("\t\t  LOJA DE INGREDIENTES  \t\t\n");
					abrirLoja();
					System.out.println("1) Comprar Ingrediente");
					System.out.println("2) Voltar ao Menu Principal\n");
					System.out.println(DIVIDER);
				}

				System.out.print("Informe a ação que deseja realizar: ");
				operation = scanner.nextInt();
				clearConsole();
				if(menu == MenuState.MAIN){
					switch (operation) {
						case 0: {
							System.out.println("Encerrando o SOAP\n");
							break;
						}
						case 1: {
							visualizarInventario();
							System.out.println("");
							break;
						}
						case 2: {
							menu = MenuState.NPCS;
							break;
						}
						case 3: {
							menu = MenuState.SHOP;
							break;
						}
					}
				} else if(menu == MenuState.NPCS){
					switch (operation) {
						case 1: {
							venderPocao(scanner);
							System.out.println("");
							break;
						}
						case 2: {
							menu = MenuState.MAIN;
							break;
						}
					}
				} else if(menu == MenuState.SHOP){
					switch (operation) {
						case 1: {
							comprarIngrediente(scanner);
							System.out.println("");
							break;
						}
						case 2: {
							menu = MenuState.MAIN;
							break;
						}
					}
				}
			}			
		} finally {
			scanner.close();
		}
	}

	private static boolean login(Scanner scanner) throws IOException {
		System.out.print("Digite o nome do jogador: ");
		String playerName = scanner.nextLine();
		
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/login/?nome=" + playerName, "GET", "");

		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		menu = MenuState.MAIN;
		return true;
	}

	private static boolean register(Scanner scanner) throws IOException {
		System.out.print("Digite o nome do jogador: ");
		String playerName = scanner.nextLine();
		
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/register/?nome=" + playerName, "POST", "");

		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		menu = MenuState.MAIN;
		return true;
	}

	private static boolean visualizarInventario() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/visualizarInventario/?id=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		return true;
	}

	private static boolean npcsAtendimento() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/npcsAtendimento/?id=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		menu = MenuState.NPCS;
		return true;
	}

	private static boolean abrirLoja() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/abrirLoja?jogadorId=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		
		return true;
	}

	private static boolean venderPocao(Scanner scanner) throws IOException {
		System.out.print("Digite o número do cliente: ");
		Integer clientNumber = scanner.nextInt();

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/venderPocao?npcId=" + clientNumber + "&jogadorId=" + jogador.getId(), "POST", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		return true;
	}

	private static boolean comprarIngrediente(Scanner scanner) throws IOException {
		System.out.print("Digite o número do ingrediente: ");
		Integer ingredientNumber = scanner.nextInt();

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/comprarIngrediente?ingredienteId=" + ingredientNumber + "&jogadorId=" + jogador.getId(), "POST", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro: " + result.getMessage() + "\n");
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		return true;
	}

	private static void clearConsole(){
		for(int i=0; i<50; i++){
			System.out.println("");
		}
	}
}