package com.technews.library.service;

import com.technews.library.dto.AdminDto;
import com.technews.library.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);

    Admin save(AdminDto adminDto);
}
