package modelo.builder;

import modelo.dao.IPersonaDao;
import modelo.dao.PersonaDaoMapDB;
import modelo.dao.PersonaDaoObjectBox;

public class PersonaDaoBuilder {

    private TipoBaseDatos tipoBaseDatos = TipoBaseDatos.MAPDB;

    // Parámetros de configuración para MapDB
    private String rutaArchivoMapDB = "datos/personas_mapdb.db";
    private String nombreMapaMapDB = "personas";
    private boolean transaccionesMapDB = true;

    // Parámetros de configuración para ObjectBox
    private String rutaDirectorioObjectBox = "datos/objectbox-personas";

    public PersonaDaoBuilder conTipoBaseDatos(TipoBaseDatos tipoBaseDatos) {
        this.tipoBaseDatos = tipoBaseDatos;
        return this;
    }

    public PersonaDaoBuilder usarMapDB() {
        this.tipoBaseDatos = TipoBaseDatos.MAPDB;
        return this;
    }

    public PersonaDaoBuilder usarObjectBox() {
        this.tipoBaseDatos = TipoBaseDatos.OBJECTBOX;
        return this;
    }

    public PersonaDaoBuilder conRutaArchivoMapDB(String rutaArchivoMapDB) {
        this.rutaArchivoMapDB = rutaArchivoMapDB;
        return this;
    }

    public PersonaDaoBuilder conNombreMapaMapDB(String nombreMapaMapDB) {
        this.nombreMapaMapDB = nombreMapaMapDB;
        return this;
    }

    public PersonaDaoBuilder conTransaccionesMapDB(boolean transaccionesMapDB) {
        this.transaccionesMapDB = transaccionesMapDB;
        return this;
    }

    public PersonaDaoBuilder conRutaDirectorioObjectBox(String rutaDirectorioObjectBox) {
        this.rutaDirectorioObjectBox = rutaDirectorioObjectBox;
        return this;
    }

    public IPersonaDao build() {
        if (tipoBaseDatos == null) {
            throw new IllegalStateException("Debe seleccionar un tipo de base de datos (MAPDB u OBJECTBOX).");
        }

        switch (tipoBaseDatos) {
            case MAPDB:
                return new PersonaDaoMapDB(rutaArchivoMapDB, nombreMapaMapDB, transaccionesMapDB);
            case OBJECTBOX:
                return new PersonaDaoObjectBox(rutaDirectorioObjectBox);
            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipoBaseDatos);
        }
    }
}