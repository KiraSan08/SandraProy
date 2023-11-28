package com.fiuni.sd.service.presencePerMatter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fiuni.sd.dao.IPresenceDao;
import com.fiuni.sd.dao.IPresencePerMatterDao;
import com.fiuni.sd.dao.IStudentDao;
import com.fiuni.sd.domain.PresenceDomain;
import com.fiuni.sd.domain.PresencePerMatterDomain;
import com.fiuni.sd.domain.StudentDomain;
import com.fiuni.sd.dto.presencePerMatter.PresencePerMatterDto;
import com.fiuni.sd.dto.presencePerMatter.PresencePerMatterListDto;
import com.fiuni.sd.exception.ResourceNotFoundException;
import com.fiuni.sd.service.base.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PresencePerMatterServiceImpl
        extends BaseServiceImpl<PresencePerMatterDto, PresencePerMatterDomain, PresencePerMatterListDto>
        implements IPresencePerMatterService {

    @Autowired
    private IStudentDao studentDao;

    @Autowired
    private IPresenceDao presenceDao;

    @Autowired
    private IPresencePerMatterDao presencePerMatterDao;

    @Override
    public PresencePerMatterDto create(PresencePerMatterDto dto) {
        PresencePerMatterDomain presencePerMatter = convertDtoToDomain(dto);
        PresencePerMatterDomain createdPresencePerMatter = presencePerMatterDao.save(presencePerMatter);
        return convertDomainToDto(createdPresencePerMatter);
    }

    @Override
    public PresencePerMatterDto getById(Integer id) {
        PresencePerMatterDomain presencePerMatter = presencePerMatterDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PresencePerMatter", "id", id));
        return convertDomainToDto(presencePerMatter);
    }

    @Override
    public PresencePerMatterDto delete(Integer id) {
        presencePerMatterDao.deleteById(id);
        // Retorna un objeto PresencePerMatterListDto o null, según sea necesario
        return null;
    }

    @Override
    public PresencePerMatterDto update(Integer id, PresencePerMatterDto dto) {
        PresencePerMatterDomain currentPresencePerMatter = presencePerMatterDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PresencePerMatter", "id", id));

        // Actualiza el atributo isPresent en PresencePerMatterDomain si es necesario
        if (dto.getIsPresent() != null) {
            currentPresencePerMatter.setIsPresent(dto.getIsPresent());
        }

        if (dto.getNotes() != null) {
            currentPresencePerMatter.setNotes(dto.getNotes());
        }

        // Si deseas actualizar las relaciones Student y Presence, puedes hacerlo así:
        if (dto.getStudentId() != null) {
            StudentDomain studentDomain = studentDao.findById(dto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));
            currentPresencePerMatter.setStudent(studentDomain);
        }

        if (dto.getPresenceId() != null) {
            PresenceDomain presenceDomain = presenceDao.findById(dto.getPresenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Presence", "id", dto.getPresenceId()));
            currentPresencePerMatter.setPresence(presenceDomain);
        }

        // Guarda los cambios en la base de datos
        currentPresencePerMatter = presencePerMatterDao.save(currentPresencePerMatter);

        // Convierte el PresencePerMatterDomain actualizado a un PresencePerMatterDto y
        // lo devuelve
        return convertDomainToDto(currentPresencePerMatter);
    }

    @Override
    public PresencePerMatterDomain convertDtoToDomain(PresencePerMatterDto dto) {

        PresencePerMatterDomain presencePerMatter = new PresencePerMatterDomain();
        presencePerMatter.setId(dto.getId());

        // Print the value of isPresent before setting it
        System.out.println("isPresent before setting: " + dto.getIsPresent());

        presencePerMatter.setIsPresent(dto.getIsPresent());

        // Print the value of isPresent after setting it
        System.out.println("isPresent after setting: " + presencePerMatter.getIsPresent());

        presencePerMatter.setNotes(dto.getNotes());

        if (dto.getPresenceId() != null) {
            PresenceDomain presenceDomain = presenceDao.findById(dto.getPresenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("PresenceDomain", "id", dto.getPresenceId()));
            presencePerMatter.setPresence(presenceDomain);
        }

        if (dto.getStudentId() != null) {
            StudentDomain studentDomain = studentDao.findById(dto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("StudentDomain", "id", dto.getStudentId()));
            presencePerMatter.setStudent(studentDomain);
        }

        return presencePerMatter;
    }

    // @Override
    // public Page<PresencePerMatterDto> get(Pageable pageable) {
    // Page<PresencePerMatterDomain> pages = presencePerMatterDao.findAll(pageable);

    // List<PresencePerMatterDto> list = new ArrayList<>();
    // pages.forEach(presencePerMatter -> {
    // PresencePerMatterDto dto = convertDomainToDto(presencePerMatter);
    // list.add(dto);
    // });

    // PresencePerMatterListDto result = new PresencePerMatterListDto();
    // result.setPresences(list);
    // result.setPage(pages.getNumber());
    // result.setTotalPages(pages.getTotalPages());
    // result.setTotal((int) pages.getTotalElements());
    // result.set_hasPrev(pages.hasPrevious());
    // result.set_hasNext(pages.hasNext());

    // return result.getPresences();
    // }

    @Override
    public PresencePerMatterListDto get(Pageable pageable) {
        PresencePerMatterListDto result = new PresencePerMatterListDto();
        List<PresencePerMatterDto> list = new ArrayList<>();
        Page<PresencePerMatterDomain> pages = presencePerMatterDao.findAll(pageable);
        pages.forEach(presencePerMatter -> {
            PresencePerMatterDto dto = convertDomainToDto(presencePerMatter);
            list.add(dto);
        });
        result.setPresencePerMatters(list);
        result.setPage(pages.getNumber());
        result.setTotalPages(pages.getTotalPages());
        result.setTotal((int) presencePerMatterDao.count());
        result.set_hasPrev(pages.hasPrevious());
        result.set_hasNext(pages.hasNext());
        return result;
    }

    @Override
    public PresencePerMatterDto convertDomainToDto(PresencePerMatterDomain domain) {
        PresencePerMatterDto dto = new PresencePerMatterDto();
        dto.setStudentId(domain.getStudent().getId());
        dto.setPresenceId((domain.getPresence().getId()));
        dto.setId(domain.getId());

        // Asigna otros atributos si es necesario
        return dto;
    }
}
