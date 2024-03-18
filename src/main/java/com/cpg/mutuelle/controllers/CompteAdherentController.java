package com.cpg.mutuelle.controllers;

import com.cpg.mutuelle.detailsServices.CpAdherentInfoDetails;
import com.cpg.mutuelle.detailsServices.JwtService;
import com.cpg.mutuelle.entities.CompteAdherent;
import com.cpg.mutuelle.entities.dto.AuthRequest;
import com.cpg.mutuelle.entities.dto.ResponseLogin;
import com.cpg.mutuelle.entities.dto.CurrentUserDto;
import com.cpg.mutuelle.entities.enumerations.Role;
import com.cpg.mutuelle.exceptions.DataNotFoundException;
import com.cpg.mutuelle.services.ICompteAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/cpAdherent")
@CrossOrigin(origins = "*")
public class CompteAdherentController {
    @Autowired
    private  ICompteAdherentService compteAdherentService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseLogin Login (@RequestBody AuthRequest authRequest){
        UserDetails user = compteAdherentService.loadUserByUsername(authRequest.getMatricule());
        if(!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
            throw new DataNotFoundException("Invalid Password");
        }else{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getMatricule(), authRequest.getPassword()));
            if(authenticate.isAuthenticated()){

                return new ResponseLogin(jwtService.generateToken(authRequest.getMatricule()));
            }else {
                throw new DataNotFoundException("Invalid username or password");
            }
        }

    }
    @PostMapping(path = "/addCpAdherent")
    //@PreAuthorize("hasAuthority('ADHERENT')")
    public ResponseEntity<CompteAdherent> addAdherent(@RequestBody CompteAdherent cpAdherent) {
        cpAdherent.setRole(Role.ADHERENT);
        CompteAdherent createdCpAdherent = compteAdherentService.createCpAdherent(cpAdherent);
        return new ResponseEntity<>(createdCpAdherent, HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/delete/{id}")
    public void deleteCompte(@PathVariable Long id){
        compteAdherentService.deleteCpAdherent(id);
    }

    @GetMapping("/api/currentAdherent")
    public CpAdherentInfoDetails getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        UserDetails userDetails = compteAdherentService.loadUserByUsername(userName);
        CpAdherentInfoDetails userInfoDetails = (CpAdherentInfoDetails) userDetails;

       /* CurrentUserDto currentUser = new CurrentUserDto();
        currentUser.setId(userInfoDetails.getAdherent().getId());
        currentUser.setNom(userInfoDetails.getAdherent().getNom());
        currentUser.setPrenom(userInfoDetails.getAdherent().getPrenom());
        currentUser.setMatricule(userInfoDetails.getAdherent().getMatricule());
        currentUser.setMail(userInfoDetails.getAdherent().getMail());
        currentUser.setTel(userInfoDetails.getAdherent().getTel());
        currentUser.setType(userInfoDetails.getAdherent().getType());
        currentUser.setEtatCivil(userInfoDetails.getAdherent().getEtatCivil());
        currentUser.setSexe(userInfoDetails.getAdherent().getSexe());
        currentUser.setDateDeNaissance(userInfoDetails.getAdherent().getDateDeNaissance());
        currentUser.setCin(userInfoDetails.getAdherent().getCin());
        currentUser.setRole(userInfoDetails.getRole());
        currentUser.setAyantsDroits(userInfoDetails.getAyantsDroits());*/

        return userInfoDetails;
    }
}
