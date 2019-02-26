package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.SongFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainViewModule.class)
public interface MainViewComponent {

     void inject(MainActivity activity);
     void inject(SongFragment songFragment);

}
