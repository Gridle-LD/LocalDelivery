package com.example.localdelivery.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.localdelivery.local.Entity.ShopsEntity;
import com.example.localdelivery.repository.NearbyShopsRepository;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;

public class NearbyShopsViewModel extends AndroidViewModel {
    private NearbyShopsRepository nearbyShopsRepository;
    private LiveData<List<ShopsEntity>> shopsList;
    private CompositeDisposable disposable = new CompositeDisposable();

    public NearbyShopsViewModel(@NonNull Application application) {
        super(application);
        nearbyShopsRepository = new NearbyShopsRepository(application);
    }

    public LiveData<List<ShopsEntity>> getShopsList() {
        shopsList = nearbyShopsRepository.getAllShops();
        return shopsList;
    }

    public CompositeDisposable getDisposable() {
        disposable = nearbyShopsRepository.getDisposable();
        return disposable;
    }

    public void fav(int pos, int like) {
        nearbyShopsRepository.fav(pos, like);
    }
}
