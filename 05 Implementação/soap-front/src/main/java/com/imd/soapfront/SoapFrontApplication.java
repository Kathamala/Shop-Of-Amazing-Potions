package com.imd.soapfront;

import java.io.IOException;
import java.util.Scanner;

import com.imd.soapfront.helper.ASCIIArts;
import com.imd.soapfront.helper.HttpRequestHandler;
import com.imd.soapfront.helper.ResultHelper;
import com.imd.soapfront.model.Jogador;
import com.imd.soapfront.model.MenuState;


public class SoapFrontApplication {

	final static String URL_BASE = "http://localhost:8080";
	final static String MAINGAME_CONTROLLER = "/soap/maingame";
	final static String DIVIDER = "#########################################################################\n";

	private static Jogador jogador;
	private static MenuState menu = MenuState.LOGIN;

	public static void main(String[] args) throws IOException {
		int operation = -1;
		Scanner scanner = new Scanner(System.in);
		clearConsole();
		try {
			//LOGIN
			while (operation != 0 & menu == MenuState.LOGIN) {
				System.out.println(DIVIDER);
				System.out.println("\t\t\tSHOP OF AMAZING PRODUCTS\n");		
				System.out.println("0) Sair do jogo");
				System.out.println("1) Entrar no jogo");
				System.out.println("2) Cadastrar jogador\n");
				System.out.println(DIVIDER);
				System.out.print("Informe a ação que deseja realizar: ");
				operation = scanner.nextInt();
				clearConsole();
				switch (operation) {
					case 0: {
						System.out.println(ASCIIArts.SLEEPING_LADY);
						System.out.println("Encerrando o SOAP. Até mais!\n"); 						
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
					System.out.println("\t\t\tSHOP OF AMAZING PRODUCTS\t\t\n");
					System.out.println("\t\t\t     MENU PRINCIPAL     \t\t\n");
					System.out.println("BEM VINDO(A), " + jogador.getNome() + "\n");
					System.out.println(ASCIIArts.SOAP_STORE);
					System.out.println("0) Sair do jogo");
					System.out.println("1) Visualizar Inventário");
					System.out.println("2) IR PARA: Clientes em Atendimento");
					System.out.println("3) IR PARA: Loja");
					System.out.println("4) Fabricar Poção\n");
					System.out.println(DIVIDER);
				} else if(menu == MenuState.NPCS){
					System.out.println(DIVIDER);
					System.out.println("\t\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
					System.out.println("\t\t\tCLIENTES EM ATENDIMENTO\t\t\n");
					System.out.println(ASCIIArts.NPCS_IN_LINE);
					npcsAtendimento();
					System.out.println("1) Vender Poção");
					System.out.println("2) Detalhar Condição");
					System.out.println("3) Voltar ao Menu Principal\n");
					System.out.println(DIVIDER);
				} else if(menu == MenuState.SHOP){
					System.out.println(DIVIDER);
					System.out.println("\t\t\tSHOP OF AMAZING PRODUCTS\t\t\n");		
					System.out.println("\t\t\t  LOJA DE INGREDIENTES  \t\t\n");
					System.out.println(ASCIIArts.SHOP_OWNER);
					abrirLoja();
					System.out.println("1) Comprar Ingrediente");
					System.out.println("2) Voltar ao Menu Principal\n");
					System.out.println(DIVIDER);
				}

				System.out.print("Informe a ação que deseja realizar: ");
				operation = scanner.nextInt();
				if(menu == MenuState.MAIN){
					switch (operation) {
						case 0: {
							clearConsole();
							System.out.println(ASCIIArts.SLEEPING_LADY);
							System.out.println("Encerrando o SOAP. Até mais!\n"); 
							break;
						}
						case 1: {
							clearConsole();
							visualizarInventario();
							System.out.println("");
							break;
						}
						case 2: {
							clearConsole();
							menu = MenuState.NPCS;
							break;
						}
						case 3: {
							clearConsole();
							menu = MenuState.SHOP;
							break;
						}
						case 4: {
							clearConsole();
							fabricarPocao(scanner);
							System.out.println("");
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
							detalharCondicao(scanner);
							System.out.println("");
							break;
						}						
						case 3: {
							clearConsole();
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
							clearConsole();
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
		scanner.nextLine();
		String playerName = scanner.nextLine();
		
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/login?name=" + playerName, "GET", "");

		if (!result.isStatus()) {
			clearConsole();
			System.out.print("Erro:" + result.getMessage());
			return false;
		}

		jogador = Jogador.fromJson(result.getMessage());
		menu = MenuState.MAIN;
		clearConsole();
		return true;
	}

	private static boolean register(Scanner scanner) throws IOException {
		System.out.print("Digite o nome do jogador: ");
		scanner.nextLine();
		String playerName = scanner.nextLine();
		
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/register?name=" + playerName, "POST", "");

		if (!result.isStatus()) {
			clearConsole();
			System.out.print("Erro:" + result.getMessage());
			return false;
		}

		jogador = Jogador.fromJson(result.getMessage());
		menu = MenuState.MAIN;
		clearConsole();
		return true;
	}

	private static boolean visualizarInventario() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/visualizarInventario?jogadorId=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro:" + result.getMessage());
			return false;
		}

		System.out.print(result.getMessage());
		return true;
	}

	private static boolean npcsAtendimento() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/npcsAtendimento?jogadorId=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro:" + result.getMessage());
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		menu = MenuState.NPCS;
		return true;
	}

	private static boolean abrirLoja() throws IOException {	
		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/abrirLoja?jogadorId=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro:" + result.getMessage());
			return false;
		}

		System.out.println(result.getMessage() + "\n");
		
		return true;
	}

	private static boolean detalharCondicao(Scanner scanner) throws IOException {
		System.out.print("Digite o número da condição: ");
		scanner.nextLine();
		Integer conditionNumber = scanner.nextInt();

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/detalharCondicao?condicaoId=" + conditionNumber, "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro:" + result.getMessage());
			return false;
		}

		clearConsole();
		System.out.println(result.getMessage() + "\n");
		return true;
	}

	private static boolean venderPocao(Scanner scanner) throws IOException {
		System.out.print("Digite o número do cliente: ");
		scanner.nextLine();
		Integer clientNumber = scanner.nextInt();

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/listarPocoesVendaNPC?jogadorId=" + jogador.getId(), "GET", "");
		
		if (!result.isStatus()) {
			System.out.println("Erro:" + result.getMessage());
			return false;
		}

		System.out.println(result.getMessage() + "\n");

		System.out.print("Digite o número da poção: ");
		scanner.nextLine();
		Integer potionNumber = scanner.nextInt();		

		result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/venderPocao?pocaoId=" + potionNumber + "&npcId=" + clientNumber + "&jogadorId=" + jogador.getId(), "POST", "");
		
		if (!result.isStatus()) {
			clearConsole();
			System.out.print("Erro:" + result.getMessage());
			return false;
		}

		clearConsole();
		System.out.println(result.getMessage() + "\n");
		return true;
	}

	private static boolean comprarIngrediente(Scanner scanner) throws IOException {
		System.out.print("Digite o número do ingrediente: ");
		scanner.nextLine();
		Integer ingredientNumber = scanner.nextInt();

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/comprarIngrediente?ingredienteId=" + ingredientNumber + "&jogadorId=" + jogador.getId(), "POST", "");
		
		if (!result.isStatus()) {
			clearConsole();
			System.out.print("Erro:" + result.getMessage());
			return false;
		}

		jogador = Jogador.fromJson(result.getMessage());
		clearConsole();
		System.out.println("Compra realizada com sucesso!");
		return true;
	}

	private static boolean fabricarPocao(Scanner scanner) throws IOException {
		System.out.println(ASCIIArts.POTION_DEN);
		visualizarInventario();

		System.out.println();
		System.out.print("Digite os ingredientes para montar a pocao (Formato: '1,2,3' / 0 para cancelar): ");
		scanner.nextLine();
		String ingredients = scanner.nextLine();

		if(ingredients.equals("0")){
			clearConsole();
			return false;
		}

		System.out.print("Digite a descricao da pocao: ");
		String potionName = scanner.nextLine();

		potionName = potionName.replaceAll(" ", "DspaceD");

		ResultHelper result = HttpRequestHandler.sendRequest(URL_BASE + MAINGAME_CONTROLLER + "/fabricarPocao?ingredientesString=" + ingredients + "&descricaoPocao=" + potionName + "&jogadorId=" + jogador.getId(), "POST", "");

		clearConsole();
		if (!result.isStatus()) {
			System.out.print("Erro:" + result.getMessage());
			return false;
		}

		System.out.print(result.getMessage());

		return true;
	}

	private static void clearConsole(){
		for(int i=0; i<70; i++){
			System.out.println("");
		}
	}
}