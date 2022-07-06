package co.com.todosistemas.prueba.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.todosistemas.prueba.domain.Actividad;
import co.com.todosistemas.prueba.repository.ActividadRepository;
import co.com.todosistemas.prueba.service.ActividadService;
import co.com.todosistemas.prueba.service.dto.ActividadDTO;
import co.com.todosistemas.prueba.service.mapper.ActividadMapper;

/**
 * Service Implementation for managing {@link Actividad}.
 */
@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {

    private final Logger log = LoggerFactory.getLogger(ActividadServiceImpl.class);

    private final ActividadRepository actividadRepository;

    private final ActividadMapper actividadMapper;

    public ActividadServiceImpl(ActividadRepository actividadRepository, ActividadMapper actividadMapper) {
        this.actividadRepository = actividadRepository;
        this.actividadMapper = actividadMapper;
    }

    @Override
    public ActividadDTO save(ActividadDTO actividadDTO) {
        log.debug("Request to save Actividad : {}", actividadDTO);
        Actividad actividad = actividadMapper.toEntity(actividadDTO);
        actividad = actividadRepository.save(actividad);
        return actividadMapper.toDto(actividad);
    }

    @Override
    public ActividadDTO update(ActividadDTO actividadDTO) {
        log.debug("Request to save Actividad : {}", actividadDTO);
        Actividad actividad = actividadMapper.toEntity(actividadDTO);
        actividad = actividadRepository.save(actividad);
        return actividadMapper.toDto(actividad);
    }

    @Override
    public Optional<ActividadDTO> partialUpdate(ActividadDTO actividadDTO) {
        log.debug("Request to partially update Actividad : {}", actividadDTO);

        return actividadRepository
            .findById(actividadDTO.getId())
            .map(existingActividad -> {
                actividadMapper.partialUpdate(existingActividad, actividadDTO);

                return existingActividad;
            })
            .map(actividadRepository::save)
            .map(actividadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actividad> findAll() {
        log.debug("Request to get all Actividads");
        return actividadRepository
            .findAll()
            .stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    // METODO PARA CONSULTAR LOS DIAS DE ATRASO QUE PUEDA TENER UNA ACTIVIDAD
    @Override
    public String diasRetraso(Long id) {
        log.debug("Request to find delay days");

        // VARIABLE A RETORNAR.
        String diasR;

        Optional<Actividad> actividad = actividadRepository.findById(id);

        // FECHA ACTUAL.
        Instant now = Instant.now();

        // SE CONSULTA LA FECHA LIMITE DE LA ACTIVDAD
        Instant fechaLimite = actividad.get().getFechaEstimadaEjecucion();

        if (fechaLimite.isBefore(now)) {
            // SE CONVIERTEN LAS FECHAS INSTANT A DATE PARA PODER CALCULAR LOS DIAS
            Date dateNow = Date.from(now);
            Date dateLimite = Date.from(fechaLimite);

            // SE RECUPERA LA FECHA EN MILISEGUNDOS SEGUN EL DATE
            Long dateNowTime = dateNow.getTime();
            Long dateLimitTime = dateLimite.getTime();

            // SE CALCULA LA DIFERENCIA
            Long diferencia = dateNowTime - dateLimitTime;

            // POR ULTIMO CONVETIRMOS LA DIFERNCIA EN MILISEGUNDOS A DIAS.
            double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
            diasR = String.format("%.0f", dias);
        } else {
            diasR = "0";
        }

        return diasR;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Actividad> findOne(Long id) {
        log.debug("Request to get Actividad : {}", id);
        return actividadRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actividad : {}", id);
        actividadRepository.deleteById(id);
    }
}
