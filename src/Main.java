import java.util.*;

class Cliente {
    String idCliente;
    String nombre;
    String tipoComprador;

    Cliente(String idCliente, String nombre, String tipoComprador) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.tipoComprador = tipoComprador;
    }
}

class Venta {
    String idVenta;
    Cliente cliente;
    String tipoEntrada;
    String asiento;
    int cantidad;

    Venta(String idVenta, Cliente cliente, String tipoEntrada, String asiento, int cantidad) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.tipoEntrada = tipoEntrada;
        this.asiento = asiento;
        this.cantidad = cantidad;
    }

    double calcularPrecio() {
        double precioBase = tipoEntrada.equalsIgnoreCase("VIP") ? 15000 : 10000;
        double descuento = 0;

        switch (cliente.tipoComprador.toLowerCase()) {
            case "estudiante": descuento = 0.1; break;
            case "tercera edad": descuento = 0.15; break;
            default: descuento = 0; break;
        }

        return precioBase * (1 - descuento);
    }
}

class SistemaReservaciones {
    final int MAX_VENTAS = 100;
    final int MAX_CLIENTES = 100;

    Venta[] ventas = new Venta[MAX_VENTAS];
    Cliente[] clientes = new Cliente[MAX_CLIENTES];
    int totalVentas = 0;
    int totalClientes = 0;

    String[] asientosVIP = {"A1", "A2", "A3", "A4", "A5"};
    String[] asientosGeneral = {"B1", "B2", "B3", "B4", "B5"};

    ArrayList<String> asientosOcupados = new ArrayList<>();


    public void realizarReservacion(String tipoEntrada, String tipoComprador, String nombreCliente, String asiento) {
        String idCliente = "C" + (totalClientes + 1);
        Cliente cliente = new Cliente(idCliente, nombreCliente, tipoComprador);
        clientes[totalClientes++] = cliente;

        String idVenta = "V" + (totalVentas + 1);
        Venta venta = new Venta(idVenta, cliente, tipoEntrada, asiento, 1);

        if (totalVentas < ventas.length) {
            ventas[totalVentas++] = venta;
            asientosOcupados.add(asiento);
        } else {
            System.out.println("Capacidad máxima de ventas alcanzada.");
        }
    }

    public String seleccionarAsiento(String tipoEntrada, Scanner sc) {
        String[] asientos = tipoEntrada.equalsIgnoreCase("VIP") ? asientosVIP : asientosGeneral;
        String asientoSeleccionado;

        do {
            System.out.print("Ingrese su asiento " + Arrays.toString(asientos) + ": ");
            asientoSeleccionado = sc.nextLine().toUpperCase().trim();

            if (!Arrays.asList(asientos).contains(asientoSeleccionado)) {
                System.out.println("Ese asiento no es válido para el tipo de entrada seleccionado.");
            } else if (asientosOcupados.contains(asientoSeleccionado)) {
                System.out.println("Ese asiento ya está ocupado. Elija otro.");
            } else {
                return asientoSeleccionado;
            }
        } while (true);
    }

    public void mostrarResumenVentas() {
        System.out.println("\n--- Resumen de Ventas ---");
        double totalIngresos = 0;
        int totalEntradas = 0;

        for (int i = 0; i < totalVentas; i++) {
            Venta venta = ventas[i];
            System.out.println("ID Venta: " + venta.idVenta);
            System.out.println("Cliente: " + venta.cliente.nombre);
            System.out.println("Tipo de Entrada: " + venta.tipoEntrada);
            System.out.println("Tipo de Comprador: " + venta.cliente.tipoComprador);
            System.out.println("Asiento: " + venta.asiento);
            double precio = venta.calcularPrecio();
            System.out.println("Precio pagado: $" + precio);
            totalIngresos += precio;
            totalEntradas += venta.cantidad;
            System.out.println("------------------------");
        }

        System.out.println("Total entradas vendidas: " + totalEntradas);
        System.out.println("Total ingresos: $" + totalIngresos);
    }

    public void editarReservacion(Scanner sc) {
        System.out.print("\nIngrese el nombre del cliente a editar: ");
        String nombre = sc.nextLine();
        Venta venta = null;

        for (int i = 0; i < totalVentas; i++) {
            if (ventas[i].cliente.nombre.equalsIgnoreCase(nombre)) {
                venta = ventas[i];
                break;
            }
        }

        if (venta == null) {
            System.out.println("No se encontró la reservación.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n--- Menú de Edición ---");
            System.out.println("1. Editar nombre");
            System.out.println("2. Editar tipo de entrada");
            System.out.println("3. Editar tipo de comprador");
            System.out.println("4. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nuevo nombre: ");
                    venta.cliente.nombre = sc.nextLine();
                    break;
                case 2:
                    System.out.print("Nuevo tipo de entrada (VIP o General): ");
                    venta.tipoEntrada = sc.nextLine();
                    break;
                case 3:
                    System.out.print("Nuevo tipo de comprador: ");
                    venta.cliente.tipoComprador = sc.nextLine();
                    break;
                case 4:
                    System.out.println("Saliendo del editor...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 4);
    }

    public void eliminarReservacion(Scanner sc) {
        System.out.print("\nIngrese el nombre del cliente a eliminar: ");
        String nombre = sc.nextLine();

        for (int i = 0; i < totalVentas; i++) {
            if (ventas[i].cliente.nombre.equalsIgnoreCase(nombre)) {
                asientosOcupados.remove(ventas[i].asiento);

                for (int j = i; j < totalVentas - 1; j++) {
                    ventas[j] = ventas[j + 1];
                }
                ventas[--totalVentas] = null;

                System.out.println("Reservación eliminada correctamente.");
                return;
            }
        }
        System.out.println("No se encontró la reservación.");
    }
}

public class Main {
    static final String NOMBRE_TEATRO = "Teatro Moro";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SistemaReservaciones sistema = new SistemaReservaciones();
        int opcion;

        do {
            System.out.println("\n--- MENU " + NOMBRE_TEATRO + " ---");
            System.out.println("1. Reservar entrada");
            System.out.println("2. Editar reservación");
            System.out.println("3. Eliminar reservación");
            System.out.println("4. Mostrar resumen de ventas");
            System.out.println("5. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Tipo de entrada (VIP o General): ");
                    String tipoEntrada = sc.nextLine();
                    System.out.print("Tipo de comprador (Estudiante, Adulto, Tercera Edad): ");
                    String tipoComprador = sc.nextLine();
                    System.out.print("Nombre del cliente: ");
                    String nombre = sc.nextLine();
                    System.out.print("Cantidad de entradas: ");
                    int cantidad = sc.nextInt(); sc.nextLine();

                    for (int i = 0; i < cantidad; i++) {
                        System.out.println("Seleccionando asiento #" + (i + 1) + " de " + cantidad);
                        String asiento = sistema.seleccionarAsiento(tipoEntrada, sc);
                        sistema.realizarReservacion(tipoEntrada, tipoComprador, nombre, asiento);
                    }
                    break;
                case 2:
                    sistema.editarReservacion(sc);
                    break;
                case 3:
                    sistema.eliminarReservacion(sc);
                    break;
                case 4:
                    sistema.mostrarResumenVentas();
                    break;
                case 5:
                    System.out.println("¡Gracias por utilizar el sistema de reservaciones!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);

        sc.close();
    }
}
