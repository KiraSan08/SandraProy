package com.fiuni.sd.service.presence;

import org.springframework.transaction.annotation.Transactional;

import com.fiuni.sd.utils.Setting;
import java.util.List;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.CachePut;
// import org.springframework.cache.annotation.Cacheable;

import com.fiuni.sd.dao.IMatterDao;
import com.fiuni.sd.dao.IPresenceDao;
import com.fiuni.sd.dao.IStudentDao;
import com.fiuni.sd.dto.presence.PresenceDto;
import com.fiuni.sd.dto.presence.PresenceListDto;
import com.fiuni.sd.dto.matter.MatterDto;
import com.fiuni.sd.exception.ResourceNotFoundException;
import com.fiuni.sd.domain.MatterDomain;
import com.fiuni.sd.domain.PresenceDomain;
import com.fiuni.sd.service.base.BaseServiceImpl;
import com.fiuni.sd.utils.Setting;

@Service
public class PresenceServiceImpl extends BaseServiceImpl<PresenceDto, PresenceDomain, PresenceListDto>
        implements IPresenceService {

    @Autowired
    private IMatterDao matterDao;

    @Autowired
    private IPresenceDao presenceDao;

    @Override
    public PresenceListDto get(Pageable pageable) {
        PresenceListDto result = new PresenceListDto();
        List<PresenceDto> list = new ArrayList<>();
        Page<PresenceDomain> pages = presenceDao.findAll(pageable);
        pages.forEach(presence -> {
            PresenceDto dto = convertDomainToDto(presence);
            list.add(dto);
        });
        result.setPresences(list);
        result.setPage(pages.getNumber());
        result.setTotalPages(pages.getTotalPages());
        result.setTotal((int) presenceDao.count());
        result.set_hasPrev(pages.hasPrevious());
        result.set_hasNext(pages.hasNext());
        return result;
    }

    @Override
    // @Cacheable(value = Setting.CACHE_NAME, key = "'presence_' + #id",
    // cacheManager = "unlimitedCacheManager")
    public PresenceDto getById(Integer id) {
        return presenceDao.findById(id).map(this::convertDomainToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Presence", "id", id));
    }

    @Override
    // @CachePut(value = Setting.CACHE_NAME, key = "'presence_' + #result.id",
    // cacheManager = "unlimitedCacheManager")
    public PresenceDto create(PresenceDto dto) {
        PresenceDomain presence = convertDtoToDomain(dto);

        PresenceDomain createdPresence = presenceDao.save(presence);
        PresenceDto createdDto = convertDomainToDto(createdPresence);
        createdDto.setId(createdPresence.getId()); // Actualiza el ID del DTO con el ID real de la entidad creada

        return createdDto;
    }

    @Override
    // @CachePut(value = Setting.CACHE_NAME, key = "'presence_' + #id", cacheManager
    // = "unlimitedCacheManager")
    public PresenceDto update(Integer id, PresenceDto dto) {
        MatterDto matterD = new MatterDto(1, "Fisica");
        PresenceDomain currentPresence = presenceDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presence", "id", id));

        // Actualiza la relación con MatterDomain si la materia no es nula
        Integer matterDto = dto.getMatterId();
        if (matterDto != null) {
            // Aquí realizas la lógica para actualizar la materia en el PresenceDomain
            currentPresence.setMatter(convertMatterDtoToDomain(matterD));
        }

        // Actualiza otros campos en PresenceDomain si es necesario
        if (dto.getDate() != null) {
            currentPresence.setDate(dto.getDate());
        }

        // Guarda los cambios en la base de datos
        currentPresence = presenceDao.save(currentPresence);

        // Convierte el PresenceDomain actualizado a un PresenceDto y lo devuelve
        return convertDomainToDto(currentPresence);
    }

    @Override
    // @CacheEvict(value = Setting.CACHE_NAME, key = "'presence_' + #id",
    // cacheManager = "unlimitedCacheManager")
    public PresenceDto delete(Integer id) {
        PresenceDto dto = presenceDao.findById(id).map(this::convertDomainToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Presence", "id", id));
        presenceDao.deleteById(id);
        return dto;
    }

    @Override
    public PresenceDto convertDomainToDto(PresenceDomain domain) {
        // MatterDto matterD = new MatterDto(1, "matematica");

        PresenceDto dto = new PresenceDto();
        dto.setMatterId(domain.getMatter().getId());
        dto.setId(domain.getId());
        // dto.setMatterId(1);
        dto.setDate(domain.getDate());

        return dto;
    }

    public MatterDomain convertMatterDtoToDomain(MatterDto matterDto) {
        MatterDomain matterDomain = new MatterDomain();
        matterDomain.setId(matterDto.getId());
        // Asigna otros atributos si es necesario
        return matterDomain;
    }

    @Override
    public PresenceDomain convertDtoToDomain(PresenceDto dto) {
        PresenceDomain domain = new PresenceDomain();
        domain.setId(dto.getId());

        MatterDomain matterDomain = matterDao.findById(dto.getMatterId())
                .orElseThrow(() -> new ResourceNotFoundException("Matter", "id", dto.getMatterId()));
        domain.setMatter(matterDomain);

        // MatterDomain matterDo= new MatterDomain();
        // matterDo.setId(1);
        // matterDo.setName("Algebra");
        // // matterDo.setSemester(");
        // // domain.setMatter(matterDo);
        domain.setDate(dto.getDate());

        return domain;
    }

}
