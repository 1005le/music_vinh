package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.PlayActivity;
import com.example.music_vinh.view.impl.SortActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = SortViewModule.class)
public interface SortViewComponent {

    void inject(SortActivity sortActivity);
}


