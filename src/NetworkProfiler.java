import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NetworkProfiler {

    // Tabela simples de OUI (primeiros 3 bytes do MAC em maiúsculo) → Fabricante
    // Você pode expandir essa tabela depois com mais OUIs do site da IEEE
    private static final Map<String, String> OUI_DATABASE = new HashMap<>();

    static {
        OUI_DATABASE.put("00-1A-2B", "Dell Inc.");           // Exemplo Dell
        OUI_DATABASE.put("00-1C-42", "Dell Inc.");           // Outro prefixo Dell
        OUI_DATABASE.put("00-50-56", "VMware, Inc.");        // VM (se você usar máquina virtual)
        OUI_DATABASE.put("00-1B-21", "Samsung Electronics"); // Samsung
        OUI_DATABASE.put("00-1E-2A", "Samsung Electronics");
        OUI_DATABASE.put("3C-07-4F", "Apple Inc.");          // iPhone/Mac
        OUI_DATABASE.put("F0-18-98", "Apple Inc.");
        OUI_DATABASE.put("00-0C-29", "VMware, Inc.");
        OUI_DATABASE.put("00-25-9C", "Cisco Systems");       // Roteador Cisco
        OUI_DATABASE.put("00-0F-AB", "TP-Link");             // TP-Link
        // Adicione mais aqui depois...
    }

    public static void main(String[] args) {

        System.out.println("=== Perfilador Watch Dogs - Inicializando ===");

        try {
            // InetAddress é a classe do Java que representa um endereço IP ou nome de host
            // getLocalHost() pega as informações da sua própria máquina
            InetAddress enderecoLocal = InetAddress.getLocalHost();

            // getHostName() retorna o nome do computador (ex: "DESKTOP-ABC123")
            String nomeHost = enderecoLocal.getHostName();

            // getHostAddress() retorna o IP da sua máquina na rede local (ex: "192.168.1.100")
            String ipLocal = enderecoLocal.getHostAddress();

            System.out.println("-----------------------------------");
            System.out.print("Seu computador:\n");
            System.out.printf("Nome do Host: %s\n", nomeHost);
            System.out.printf("IP local: %s\n", ipLocal);
            System.out.println("-----------------------------------");

        }catch (UnknownHostException e){
            // Isso acontece se o Java não conseguir identificar sua própria máquina (muito raro)
            System.out.println("Erro ao obter informações do host local: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o prefixo da rede (Ex: 192.168.1): ");
        String prefixoRede = sc.nextLine().trim();

        System.out.println("--------------------------------------");
        System.out.println("Varredura incializada na rede "+ prefixoRede + ".0/24...");
        System.out.println("Carregando...\n");

        int dispositivosEncontrados = 0;

        // Loop que vai de 1 até 254 (os endereços válidos na sua rede local)
        for (int i = 0; i <= 254; i++) {
            String ipAtual = prefixoRede + "." + i;

            try {
                // InetAddress.getByName() cria um objeto de endereço IP a partir de uma String
                InetAddress endereco = InetAddress.getByName(ipAtual);

                // isReachable(1000) tenta pingar o dispositivo por no máximo 1000 milissegundos (1 segundo)
                // Retorna true se o dispositivo respondeu
                boolean alcancavel = endereco.isReachable(1000);

                if (alcancavel) {
                    dispositivosEncontrados++;

                    // Tenta pegar o nome do host (pode demorar um pouco)
                    String nomeHost = endereco.getHostName();

                    // Se o nome for igual ao IP, significa que não conseguiu resolver o hostname
                    if(nomeHost.equals(ipAtual)){
                        nomeHost = "(nome não resolvido)";
                    }

                    // === NOVA FUNÇÃO: Pega MAC Address ===
                    String macAddress = getMacAddress(ipAtual);
                    String fabricante = getFabricante(macAddress);

                    System.out.println("Dispositivo encontrado!");
                    System.out.println("Nome > " + nomeHost);
                    System.out.println("IP > " + ipAtual);
                    System.out.println("Mac > " + macAddress);
                    System.out.println("Fabricante > " + fabricante);
                    System.out.println("-----------------------------------");
                }
            }catch (UnknownHostException e){
                System.out.println("Erro com ip "+ ipAtual+": "+e.getMessage());
            }catch (Exception e){
                System.out.println("Erro desconhecido: "+e.getMessage());
            }
        }

        sc.close();
        System.out.println("\n=== Varredura finalizada! ===");
        System.out.println("Dispositivos encontrados: " + dispositivosEncontrados);
        System.out.println("=== Perfilador Watch Dogs - Finalizando ===");
    }
    // ====================== MÉT0DO NOVO 1: getMacAddress ======================
    private static String getMacAddress(String ip) {
        try {
            // Executa comando ARP do sistema operacional
            String comando = System.getProperty("os.name").toLowerCase().contains("win")
                    ? "arp -a " + ip          // Windows
                    : "arp -n " + ip;         // Linux/Mac

            Process processo = Runtime.getRuntime().exec(comando);
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(processo.getInputStream()));

            String linha;
            while ((linha = reader.readLine()) != null) {
                // Procura linha que contém o IP
                if (linha.contains(ip)) {
                    // Extrai o MAC (formato xx-xx-xx-xx-xx-xx ou xx:xx:xx:xx:xx:xx)
                    String[] partes = linha.trim().split("\\s+");
                    for (String parte : partes) {
                        if (parte.matches("([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2}")) {
                            return parte.toUpperCase().replace(":", "-");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Se der erro (ex: permissão), retorna vazio
        }
        return "";
    }

    // ====================== MÉT0DO NOVO 2: getFabricante ======================
    private static String getFabricante(String mac) {
        if (mac.isEmpty()) return "(desconhecido)";

        // Pega só os primeiros 8 caracteres (00-1A-2B)
        String oui = mac.substring(0, 8);
        return OUI_DATABASE.getOrDefault(oui, "Fabricante desconhecido");
    }
}
