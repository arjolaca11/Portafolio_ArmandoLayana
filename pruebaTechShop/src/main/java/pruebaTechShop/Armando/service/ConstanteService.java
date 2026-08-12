package pruebaTechShop.Armando.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pruebaTechShop.Armando.domain.Constante;
import pruebaTechShop.Armando.repository.ConstanteRepository;

@Service
public class ConstanteService {

    private final ConstanteRepository constanteRepository;

    public ConstanteService(ConstanteRepository constanteRepository) {
        this.constanteRepository = constanteRepository;
    }

    // lee un parametro de configuracion de la tabla "constante"; si no existe, se usa el valor por defecto
    @Transactional(readOnly = true)
    public String getValor(String atributo, String porDefecto) {
        return constanteRepository.findByAtributo(atributo)
                .map(c -> c.getValor())
                .orElse(porDefecto);
    }

    @Transactional(readOnly = true)
    public Optional<Constante> findByAtributo(String atributo) {
        return constanteRepository.findByAtributo(atributo);
    }

    @Transactional(readOnly = true)
    public List<Constante> getConstantes() {
        var lista = constanteRepository.findAll();
        return lista;
    }

    @Transactional(readOnly = true)
    public Constante getConstante(Integer idConstante) {
        return constanteRepository.findById(idConstante).orElseThrow(
                () -> new NoSuchElementException("Constante con ID " + idConstante + " no encontrada."));
    }

    @Transactional
    public void save(Constante constante) {
        constanteRepository.save(constante);
    }

    @Transactional
    public void delete(Integer idConstante) {
        // Verifica si la constante existe antes de intentar eliminarla
        if (!constanteRepository.existsById(idConstante)) {
            throw new IllegalArgumentException("La Constante con ID " + idConstante + " no existe.");
        }
        try {
            constanteRepository.deleteById(idConstante);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la constante. Tiene datos asociados.", e);
        }
    }
}
