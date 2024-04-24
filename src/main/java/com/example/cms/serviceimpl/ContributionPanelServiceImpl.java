package com.example.cms.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.dto.ContributionPanelResponse;
import com.example.cms.dto.UserResponse;
import com.example.cms.entity.ContributionPanel;
import com.example.cms.entity.Users;
import com.example.cms.repository.BlogsRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.repository.UsersRepository;
import com.example.cms.security.CustomeUserDetailservice;
import com.example.cms.service.ContributionPanelService;
import com.example.cms.util.IllegalAccessRequestException;
import com.example.cms.util.PanelNotFoundByIdException;
import com.example.cms.util.Responstructure;
import com.example.cms.util.UNAUTHORIZEDException;
import com.example.cms.util.UserNotFoundByIdException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContributionPanelServiceImpl implements ContributionPanelService {

	private Responstructure<ContributionPanelResponse> structure;
   	private UsersRepository userRepo;
	private BlogsRepository blogRepo;
	private ContributionPanelRepository panelRepo;

	private ContributionPanelResponse mapToResponse(ContributionPanel panel) {

		List<Users> users = panel.getUsers();
		ContributionPanelResponse panels = new ContributionPanelResponse();
		ArrayList<UserResponse> list = new ArrayList<>();
		for (Users user : users) {
			UserResponse resounse = UserResponse.builder().userId(user.getUserId()).userName(user.getUserName())
					.email(user.getEmail()).createdAt(user.getCreatedAt()).lastModifiedAt(user.getLastModifiedAt())
					.build();
			list.add(resounse);

		}
		panels.setPanelId(panel.getPanelId());
		panels.setResponse(list);

		return panels;
	}

	@Override
	public ResponseEntity<Responstructure<ContributionPanelResponse>> addUserToContribution(int userId, int panelId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email).map(owner -> {
			return panelRepo.findById(panelId).map(panel -> {
				if (!blogRepo.existsByUserAndPanel(owner, panel))
					throw new UNAUTHORIZEDException("Fiald To Add Contribut.and owner should be login");

				return userRepo.findById(userId).map(contributer -> {
					if (panel.getUsers().contains(contributer) == true) {

						throw new IllegalAccessRequestException("Fiald To Add Contribute");
					}
					panel.getUsers().add(contributer);
					panelRepo.save(panel);
					return ResponseEntity.ok(structure.setStatusCode(HttpStatus.OK.value())
							.setMessage("contributer si added").setData(mapToResponse(panel)));
				}).orElseThrow(() -> new UserNotFoundByIdException("user Not Found By Given Id"));
			}).orElseThrow(() -> new PanelNotFoundByIdException("panel not found By GivenId"));
		}).get();

	}

	@Override
	public ResponseEntity<Responstructure<ContributionPanelResponse>> removeUserFromContribution(int userId,
			int panelId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email).map(owner -> {
			return panelRepo.findById(panelId).map(panel -> {
				if (!blogRepo.existsByUserAndPanel(owner, panel))
					throw new UNAUTHORIZEDException("Fiald To remove Contribute");
				// throw new IllegalAccessRequestException("Fiald To Add Contribute");
				return userRepo.findById(userId).map(contributer -> {
					panel.getUsers().remove(contributer);
					panelRepo.save(panel);
					return ResponseEntity.ok(structure.setStatusCode(HttpStatus.OK.value())
							.setMessage("contributer si added").setData(mapToResponse(panel)));
				}).orElseThrow(() -> new UserNotFoundByIdException("user Not Found By Given Id"));
			}).orElseThrow(() -> new PanelNotFoundByIdException("panel not found By GivenId"));
		}).get();

	}

}
