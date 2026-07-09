package pruebaTechShop.Armando.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
