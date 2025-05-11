import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static String[] nombresClientes = new String[100];
    static String[] tiposComprador = new String[100];
    static int[] edades = new int[100];

    static String[] tiposEntrada = new String[100];
    static String[] asientos = new String[100];
    static double[] precios = new double[100];

    static int totalReservas = 0;

    static String[] asientosVIP = {"A1", "A2", "A3", "A4", "A5"};
    static String[] asientosPalco = {"P1", "P2", "P3", "P4", "P5"};
    static String[] asientosPlateaBaja = {"B1", "B2", "B3", "B4", "B5"};
    static String[] asientosPlateaAlta = {"PA1", "PA2", "PA3", "PA4", "PA5"};
    static String[] asientosGaleria = {"G1", "G2", "G3", "G4", "G5"};

    static ArrayList<String> asientosOcupados = new ArrayList<>();


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENÚ Teatro Moro ---");
            System.out.println("1. Reservar entrada");
            System.out.println("2. Editar reservación");
            System.out.println("3. Eliminar reservación");
            System.out.println("4. Mostrar resumen de ventas");
            System.out.println("5. Buscar boleta");
            System.out.println("6. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1:
                    reservarEntrada(sc);
                    break;
                case 2:
                    editarReservacion(sc);
                    break;
                case 3:
                    eliminarReservacion(sc);
                    break;
                case 4:
                    mostrarResumen();
                    break;
                case 5:
                    buscarBoleta(sc);
                    break;
                case 6:
                    System.out.println("¡Gracias por utilizar el sistema!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
        sc.close();
    }

    static void reservarEntrada(Scanner sc) {
        System.out.print("Tipo de entrada (VIP, Palco, Platea baja, Platea alta o Galería): ");
        String tipoEntrada = sc.nextLine().toLowerCase();

        if (!Arrays.asList("vip", "palco", "platea baja", "platea alta", "galería").contains(tipoEntrada)) {
            System.out.println("Tipo de entrada inválido.");
            return;
        }

        System.out.print("Tipo de comprador (niño, mujer, adulto, estudiante, otros): ");
        String tipoComprador = sc.nextLine().toLowerCase();
        System.out.print("Edad: ");
        int edad = sc.nextInt(); sc.nextLine();
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Cantidad de entradas: ");
        int cantidad = sc.nextInt(); sc.nextLine();

        for (int i = 0; i < cantidad; i++) {
            System.out.println("Seleccionando asiento #" + (i + 1));
            String asiento = seleccionarAsiento(tipoEntrada, sc);
            double precio = calcularPrecio(tipoEntrada, tipoComprador, edad);

            nombresClientes[totalReservas] = nombre;
            tiposComprador[totalReservas] = tipoComprador;
            edades[totalReservas] = edad;
            tiposEntrada[totalReservas] = tipoEntrada;
            asientos[totalReservas] = asiento;
            precios[totalReservas] = precio;
            asientosOcupados.add(asiento);

            totalReservas++;
        }
    }

    static double calcularPrecio(String tipoEntrada, String tipoComprador, int edad) {
        double precioBase = switch (tipoEntrada) {
            case "vip" -> 800;
            case "palco" -> 700;
            case "platea baja" -> 600;
            case "platea alta" -> 500;
            case "galería" -> 400;
            default -> 0;
        };

        double descuento = 0;
        switch (tipoComprador) {
            case "niño" -> {
                if (edad <= 13) descuento = 0.10;
            }
            case "mujer" -> {
                if (edad >= 18) descuento = 0.20;
            }
            case "estudiante" -> descuento = 0.15;
            case "adulto" -> {
                if (edad >= 50) descuento = 0.25;
            }
        }
        return precioBase * (1 - descuento);
    }

    static String seleccionarAsiento(String tipoEntrada, Scanner sc) {
        String[] disponibles = switch (tipoEntrada) {
            case "vip" -> asientosVIP;
            case "palco" -> asientosPalco;
            case "platea baja" -> asientosPlateaBaja;
            case "platea alta" -> asientosPlateaAlta;
            case "galería" -> asientosGaleria;
            default -> new String[0];
        };

        String asiento;
        do {
            System.out.print("Seleccione asiento " + Arrays.toString(disponibles) + ": ");
            asiento = sc.nextLine().toUpperCase();
            if (!Arrays.asList(disponibles).contains(asiento)) {
                System.out.println("Asiento no válido.");
            } else if (asientosOcupados.contains(asiento)) {
                System.out.println("Asiento ocupado.");
            } else {
                return asiento;
            }
        } while (true);
    }

    static void mostrarResumen() {
        double total = 0;
        System.out.println("\n--- RESUMEN DE VENTAS ---");
        for (int i = 0; i < totalReservas; i++) {
            System.out.println("Cliente: " + nombresClientes[i]);
            System.out.println("Tipo de Entrada: " + tiposEntrada[i]);
            System.out.println("Tipo de Comprador: " + tiposComprador[i]);
            System.out.println("Asiento: " + asientos[i]);
            System.out.println("Precio pagado: $" + precios[i]);
            System.out.println("--------------------------");
            total += precios[i];
        }
        System.out.println("Total de entradas vendidas: " + totalReservas);
        System.out.println("Total de ingresos: $" + total);
    }

    static void editarReservacion(Scanner sc) {
        System.out.print("Ingrese el nombre del cliente a editar: ");
        String nombre = sc.nextLine();

        for (int i = 0; i < totalReservas; i++) {
            if (nombresClientes[i].equalsIgnoreCase(nombre)) {
                System.out.println("1. Editar nombre");
                System.out.println("2. Editar tipo de entrada");
                System.out.println("3. Editar tipo de comprador");
                System.out.print("Seleccione una opción: ");
                int opcion = sc.nextInt(); sc.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Nuevo nombre: ");
                        nombresClientes[i] = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("Nuevo tipo de entrada: ");
                        tiposEntrada[i] = sc.nextLine().toLowerCase();
                        precios[i] = calcularPrecio(tiposEntrada[i], tiposComprador[i], edades[i]);
                        break;
                    case 3:
                        System.out.print("Nuevo tipo de comprador: ");
                        tiposComprador[i] = sc.nextLine().toLowerCase();
                        precios[i] = calcularPrecio(tiposEntrada[i], tiposComprador[i], edades[i]);
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
                return;
            }
        }
        System.out.println("Reservación no encontrada.");
    }

    static void eliminarReservacion(Scanner sc) {
        System.out.print("Ingrese el nombre del cliente a eliminar: ");
        String nombre = sc.nextLine();

        for (int i = 0; i < totalReservas; i++) {
            if (nombresClientes[i].equalsIgnoreCase(nombre)) {
                asientosOcupados.remove(asientos[i]);

                for (int j = i; j < totalReservas - 1; j++) {
                    nombresClientes[j] = nombresClientes[j + 1];
                    tiposComprador[j] = tiposComprador[j + 1];
                    edades[j] = edades[j + 1];
                    tiposEntrada[j] = tiposEntrada[j + 1];
                    asientos[j] = asientos[j + 1];
                    precios[j] = precios[j + 1];
                }
                totalReservas--;
                System.out.println("Reservación eliminada.");
                return;
            }
        }
        System.out.println("Reservación no encontrada.");
    }
    static void buscarBoleta(Scanner sc) {
        System.out.println("Ingrese el nombre del cliente " );
        String nombre = sc.nextLine();
        boolean encontrado = false;

        for (int i = 0; i < totalReservas; i++) {
            if (nombresClientes[i].equalsIgnoreCase(nombre)) {
                encontrado= true;
                System.out.println("\n--- BOLETA ---");
                System.out.println("Cliente: " + nombresClientes[i]);
                System.out.println("Tipo de Entrada: " + tiposEntrada[i])
                System.out.println("Tipo de Comprador: " + tiposComprador[i]);
                System.out.println("Asiento: " + asientos[i]);
                System.out.println("Precio pagado: $" + precios[i]);

            }
        }

        if(!encontrado) {
            System.out.println("No se encontro la boleta");
        }

    }
}
