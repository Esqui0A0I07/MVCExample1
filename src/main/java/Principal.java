import controlador.Coordinador;
import modelo.Logica;
import modelo.builder.PersonaDaoBuilder;
import modelo.dao.IPersonaDao;
import vista.VentanaBuscar;
import vista.VentanaPrincipal;
import vista.VentanaRegistro;

public class Principal {

    Logica miLogica;
    VentanaPrincipal miVentanaPrincipal;
    VentanaBuscar miVentanaBuscar;
    VentanaRegistro miVentanaRegistro;
    Coordinador miCoordinador;

    public static void main(String[] args) {
        Principal miPrincipal = new Principal();
        miPrincipal.iniciar();
    }

    /**
     * Permite instanciar todas las clases con las que trabaja el sistema
     */
    private void iniciar() {

        IPersonaDao personaDao = new PersonaDaoBuilder()
                .usarObjectBox()
                .conRutaDirectorioObjectBox("datos/objectbox-personas")
                .build();

        /* Cierre seguro de la base de datos al finalizar la aplicación */
        Runtime.getRuntime().addShutdownHook(new Thread(personaDao::cerrar));

        /* Se instancian las clases */
        miVentanaPrincipal = new VentanaPrincipal();
        miVentanaRegistro = new VentanaRegistro();
        miVentanaBuscar = new VentanaBuscar();
        miLogica = new Logica();
        miCoordinador = new Coordinador();

        /* Se inyecta el DAO construido en la capa de lógica */
        miLogica.setPersonaDao(personaDao);

        /* Se establecen las relaciones entre clases */
        miVentanaPrincipal.setCoordinador(miCoordinador);
        miVentanaRegistro.setCoordinador(miCoordinador);
        miVentanaBuscar.setCoordinador(miCoordinador);
        miLogica.setCoordinador(miCoordinador);

        /* Se establecen relaciones con la clase coordinador */
        miCoordinador.setMiVentanaPrincipal(miVentanaPrincipal);
        miCoordinador.setMiVentanaRegistro(miVentanaRegistro);
        miCoordinador.setMiVentanaBuscar(miVentanaBuscar);
        miCoordinador.setMiLogica(miLogica);

        miVentanaPrincipal.setVisible(true);
    }
}