package modelo.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import modelo.vo.PersonaVo;

@Entity
public class PersonaEntity {

    @Id(assignable = true)
    private long id;
    private String nombre;
    private int edad;
    private String profesion;
    private int telefono;

    public PersonaEntity() {
    }

    public PersonaEntity(long id, String nombre, int edad, String profesion, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.profesion = profesion;
        this.telefono = telefono;
    }

    public static PersonaEntity desdeVo(PersonaVo vo) {
        return new PersonaEntity(
                vo.getIdPersona(),
                vo.getNombrePersona(),
                vo.getEdadPersona(),
                vo.getProfesionPersona(),
                vo.getTelefonoPersona()
        );
    }

    public PersonaVo aVo() {
        PersonaVo vo = new PersonaVo();
        vo.setIdPersona((int) this.id);
        vo.setNombrePersona(this.nombre);
        vo.setEdadPersona(this.edad);
        vo.setProfesionPersona(this.profesion);
        vo.setTelefonoPersona(this.telefono);
        return vo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}