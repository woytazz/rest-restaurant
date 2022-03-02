package pl.pas.rest.repository;

import pl.pas.rest.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class UserRepository implements Repository<User> {
    private final List<User> userList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean add(User obj) {
        if (findByLogin(obj.getLogin()).isEmpty()) {
            obj.setUuid(UUID.randomUUID());
            return userList.add(obj);
        } else {
            return false;
        }
    }

    @Override
    public Optional<User> get(UUID uuid) {
        for (User user : userList) {
            if (user.getUuid().equals(uuid)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> getList() {
        return Collections.unmodifiableList(this.userList);
    }

    @Override
    public boolean update(UUID uuid, User obj) {
        Optional<User> optionalUser = get(uuid);
        if (optionalUser.isPresent()) {
            optionalUser.get().setName(obj.getName());
            optionalUser.get().setSurname(obj.getSurname());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
       return userList.removeIf(user -> user.getUuid().equals(uuid));
    }

    public Optional<User> findByLogin(String login) {
        for (User user : userList) {
            if (user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // To AUTH
    public User findByLoginPasswordActive(String login, String password) {
        return userList.stream()
                .filter(user -> (login.equals(user.getLogin()) && password.equals(user.getPassword()) && user.getIsActive()))
                .findAny()
                .orElse(null);
    }

    public void changePassword(User obj, String password) {
        obj.setPassword(password);
    }
}
