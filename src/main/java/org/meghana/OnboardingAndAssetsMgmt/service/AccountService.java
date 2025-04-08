package org.meghana.OnboardingAndAssetsMgmt.service;

import org.meghana.OnboardingAndAssetsMgmt.model.Account;
import org.meghana.OnboardingAndAssetsMgmt.repository.AccountRepository;
import org.meghana.OnboardingAndAssetsMgmt.util.constants.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class AccountService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getAuthorities() == null){
            account.setAuthorities(Authority.USER.toString());
        }
        account.setApproved(false);
        return accountRepository.save(account);
        
    }

    public List<Account> findall(){

        return accountRepository.findAll();

    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
        
    }

    public Optional<Account> findByID(String id) {
        return accountRepository.findById(id);
        
    }

    public void deleteByID(String id) {
        accountRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<Account> optionaAccount =  accountRepository.findByEmail(email);
       if (!optionaAccount.isPresent()){
            throw new UsernameNotFoundException("Account not found");
       }
       Account account = optionaAccount.get();

       List<GrantedAuthority> grantedAuthoriy = new ArrayList<>();
       grantedAuthoriy.add(new SimpleGrantedAuthority(account.getAuthorities()));
       return new User(account.getEmail(), account.getPassword(), grantedAuthoriy);
    }

}
