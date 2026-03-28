# 🕵️‍♂️ Perfilador Watch Dogs - Java Edition

**Ferramenta de Perfilamento de Rede Local** inspirada no protagonista Aiden Pearce (Watch Dogs).

Um scanner simples e educativo em Java que permite visualizar dispositivos conectados na rede local, mostrando:
- Endereço IP
- Nome do host (quando disponível)
- Endereço MAC
- Fabricante do equipamento (Samsung, Dell, Apple, TP-Link, etc.)

Projeto pessoal desenvolvido durante o curso de **Análise e Desenvolvimento de Sistemas na UNA** e as trilhas de **Cibersegurança da Cisco Networking Academy**.

---

## ✅ Funcionalidades atuais (3/6)

- Detecção do IP local
- Scan da faixa de rede `/24`
- Captura de MAC Address via ARP
- Identificação do fabricante via OUI lookup
- Interface via linha de comando

**Em desenvolvimento:**
- Interface gráfica (Swing)
- Exportação para CSV
- Banco maior de fabricantes

---

## 🛠️ Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/watchdogs-profiler-java.git
2. Abra o projeto em sua IDE (no meu caso foi o IntelliJ).
3. Execute a classe src/NetworkProfiler.java.
4. Informe o prefixo da sua rede (exemplo: 192.168.1).

Requisito: Java 8 ou superior.

⚠️ Uso Ético
Esta ferramenta é exclusivamente educacional.
Use apenas em sua própria rede ou em ambientes de teste com autorização.

📸 Screenshots
(Adicione as imagens aqui depois de tirar prints)

🛠️ Tecnologias utilizadas

- Java Standard Edition
- java.net.InetAddress
- Comando ARP do sistema operacional
- HashMap para lookup de OUI

**Outras infromações:**

- Desenvolvido por: Carlos Eduardo da Silva - 3ZK4L
- Status: Em desenvolvimento (Etapa 3 de 6)
- Inspiração: Watch Dogs + Cibersegurança