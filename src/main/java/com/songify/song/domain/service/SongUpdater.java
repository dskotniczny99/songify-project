package com.songify.song.domain.service;

import com.songify.song.domain.model.Song;
import com.songify.song.domain.repository.SongRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Transactional
public class SongUpdater {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;
    private final SongAdder songAdder;

    SongUpdater(SongRepository songRepository, SongRetriever songRetriever, SongAdder songAdder) {
        this.songRepository = songRepository;
        this.songRetriever = songRetriever;
        this.songAdder = songAdder;
    }

    public void updateById(Long id, Song newSong) {
        songRetriever.existsById(id);
        songRepository.updateById(id, newSong);
    }

    public void someComplicatedLogic() {
        songRepository.updateById(1L, new Song("Hej!", "Kris"));
        songRepository.updateById(2L, new Song("Hej!", "Kris"));
        songRepository.updateById(3L, new Song("Hej!", "Kris"));
        Song dogSong = songAdder.addSong(new Song("dog", "cat"));
        songRepository.updateById(dogSong.getId(), new Song("dog2","dog2"));
    }

    public Song updatePartiallyById(Long id, Song songFromRequest) {
        Song songFromDatabase = songRetriever.findSongById(id);
        Song.SongBuilder builder = Song.builder();
        if (songFromRequest.getName() != null) {
            builder.name(songFromRequest.getName());
        } else {
            builder.name(songFromDatabase.getName());
        }
        if (songFromRequest.getArtist() != null) {
            builder.artist(songFromRequest.getArtist());
        } else {
            builder.artist(songFromDatabase.getArtist());
        }
        Song toSave = builder.build();
        updateById(id, toSave);
        return toSave;
    }

}
