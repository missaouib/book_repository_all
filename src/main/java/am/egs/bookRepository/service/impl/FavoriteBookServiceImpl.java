package am.egs.bookRepository.service.impl;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.repository.FavoriteBookRepository;
import am.egs.bookRepository.service.FavoriteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteBookServiceImpl implements FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;

    @Autowired
    public FavoriteBookServiceImpl(FavoriteBookRepository favoriteBookRepository) {
        this.favoriteBookRepository = favoriteBookRepository;
    }


    @Override
    public void save(FavoriteBook favoriteBook) {
        favoriteBookRepository.save(favoriteBook);
    }

    @Override
    public FavoriteBook findByUserAndBook(User user, Book book) {
        return favoriteBookRepository.findByUserAndBook(user, book);
    }


    @Override
    public Object getOne(Long id) {
        return favoriteBookRepository.getOne(id);
    }
}
