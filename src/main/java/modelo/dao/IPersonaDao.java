package modelo.dao;

import modelo.vo.PersonaVo;

public interface IPersonaDao {

    void registrarPersona(PersonaVo miPersona);

    PersonaVo buscarPersona(int codigo);

    void modificarPersona(PersonaVo miPersona);

    void eliminarPersona(String codigo);

    void cerrar();
}