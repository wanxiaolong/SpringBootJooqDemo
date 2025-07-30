package com.demo.jooq.service;

import com.demo.jooq.model.Movie;
// 导入jOOQ生成的Record类
import com.demo.jooq.codegen.tables.records.MoviesRecord;
// jOOQ的核心接口
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// 导入jOOQ生成的Table类，方便引用表和字段
import static com.demo.jooq.codegen.tables.Movies.MOVIES;

@Service
@Transactional
public class MovieService {

    // 注入jOOQ的DSLContext
    private final DSLContext dslContext;

    public MovieService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    // --- C: Create (创建新电影) ---
    public Movie createMovie(Movie movie) {
        MoviesRecord moviesRecord = dslContext.insertInto(MOVIES)
                // 注意：jOOQ生成的字段名是列名的小写和下划线
                .set(MOVIES.YEAR, movie.getYear())
                .set(MOVIES.TITLE, movie.getTitle())
                .set(MOVIES.DIRECTOR, movie.getDirector())
                .set(MOVIES.GENRE, movie.getGenre())
                .set(MOVIES.LIKES, movie.getLikes() != null ? movie.getLikes() : 0)
                // 返回生成的主键ID
                .returning(MOVIES.ID)
                // 执行并获取结果
                .fetchOne();

        if (moviesRecord != null) {
            // 将生成的ID设置回Movie对象
            movie.setId(moviesRecord.getId());
            System.out.println("Created movie: " + movie.getTitle() + " with ID: " + movie.getId());

            // 将 Record 映射回 Movie POJO 并返回
            return moviesRecord.into(Movie.class);
        }
        return movie;
    }

    // --- R: Read (读取电影) ---
    public Optional<Movie> getMovieById(Integer id) {
        return dslContext.selectFrom(MOVIES)
                .where(MOVIES.ID.eq(id))
                // 将查询结果映射回Movie对象
                .fetchOptionalInto(Movie.class);
    }

    // --- R: 查询电影 (按年份查询) ---
    public List<Movie> getMoviesByYear(Integer year) {
        return dslContext.selectFrom(MOVIES)
                .where(MOVIES.YEAR.eq(year))
                // 映射为Movie对象列表
                .fetchInto(Movie.class);
    }

    // --- R: 按照年份查询(sql: in)
    public List<Movie> getMoviesByYearIn(Set<Integer> years) {
        return dslContext.selectFrom(MOVIES)
                .where(MOVIES.YEAR.in(years))
                .fetchInto(Movie.class);
    }

    // --- R: 分页查询所有电影 ---
    public List<Movie> getAllMoviesPaged(int page, int size) {
        // 计算 OFFSET
        int offset = page * size;
        System.out.println("Fetching movies - Page: " + page + ", Size: " + size + ", Offset: " + offset);
        return dslContext.selectFrom(MOVIES)
                // 重要：分页查询需要一个明确的排序顺序，否则结果不可预测
                .orderBy(MOVIES.ID.asc())
                // 限制每页返回的记录数
                .limit(size)
                // 从哪里开始返回记录
                .offset(offset)
                .fetchInto(Movie.class);
    }

    // --- R: 查询所有电影 ---
    public List<Movie> getAllMovies() {
        return dslContext.selectFrom(MOVIES)
                // 映射为Movie对象列表
                .fetchInto(Movie.class);
    }

    // --- U: Update (更新电影属性) ---
    public Optional<Movie> updateMovie(Integer id, Movie movieDetails) {
        int updatedRows = dslContext.update(MOVIES)
                .set(MOVIES.YEAR, movieDetails.getYear())
                .set(MOVIES.TITLE, movieDetails.getTitle())
                .set(MOVIES.DIRECTOR, movieDetails.getDirector())
                .set(MOVIES.GENRE, movieDetails.getGenre())
                .set(MOVIES.LIKES, movieDetails.getLikes())
                .where(MOVIES.ID.eq(id))
                // 执行更新操作
                .execute();

        if (updatedRows > 0) {
            System.out.println("Updated movie with ID: " + id);
            // 更新成功后，再获取最新的数据
            return getMovieById(id);
        }
        return Optional.empty();
    }

    // --- U: 原子计数器 (增加点赞数) ---
    public Optional<Movie> incrementMovieLikes(Integer id) {
        int updatedRows = dslContext.update(MOVIES)
                // 使用jOOQ的plus方法进行原子递增
                .set(MOVIES.LIKES, MOVIES.LIKES.plus(1))
                .where(MOVIES.ID.eq(id))
                .execute();

        if (updatedRows > 0) {
            System.out.println("Incremented likes for movie ID: " + id);
            return getMovieById(id);
        }
        return Optional.empty();
    }

    // --- D: Delete (删除电影) ---
    public void deleteMovie(Integer id) {
        dslContext.deleteFrom(MOVIES)
                .where(MOVIES.ID.eq(id))
                .execute();
        System.out.println("Deleted movie with ID: " + id);
    }

    // --- D: 删除所有电影 ---
    public void deleteAllMovies() {
        dslContext.deleteFrom(MOVIES).execute();
        System.out.println("Deleted all movies.");
    }
}