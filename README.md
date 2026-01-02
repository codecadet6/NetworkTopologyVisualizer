# Network Topology Visualizer (Java Swing)

## Overview
This is a **Java Swing-based Network Topology Visualizer** that scans your local network (LAN) and displays a graphical map showing your router, connected devices, and your PC. It provides a professional, interactive visualization with colored nodes, connection lines, and tooltips.

## Features
- Scan local subnet for devices.
- Visualize the network topology with:
  - Router (green node)
  - Connected devices (blue nodes)
  - Your PC (purple node)
- Interactive tooltips: hover over a node to see its IP.
- Professional GUI with gradient background and styled nodes.
- Simple and easy to run using Java 1.8+.

## Technology Stack
- **Language:** Java  
- **GUI Framework:** Swing  
- **Networking:** `InetAddress` for ping simulation  
- **IDE/Platform:** Any IDE (Eclipse, IntelliJ, NetBeans) or Command Prompt/Terminal

## How to Run
Clone the repository and navigate to the project folder, then compile and run the program:

```bash
git clone <repository-url>
cd Network-Topology-Visualizer
javac TopologyGUI.java
java TopologyGUI
