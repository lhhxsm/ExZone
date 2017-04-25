package com.exzone.lib.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间: 2016/10/11.
 */
public class FrescoLoader extends BaseImageLoader<DraweeView, FrescoLoader.FrescoOption> {

  public static void requestImage(Context context, Uri uri, final float currentPosition,
      final RequestCallback callback) {
    ImageRequest request =
        ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
    ImagePipeline pipeline = Fresco.getImagePipeline();
    DataSource<CloseableReference<CloseableImage>> source =
        pipeline.fetchDecodedImage(request, context);
    source.subscribe(new BaseBitmapDataSubscriber() {
      @Override protected void onNewResultImpl(@Nullable Bitmap bitmap) {
        callback.onSuccess(bitmap, currentPosition);
      }

      @Override
      protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

      }
    }, CallerThreadExecutor.getInstance());
  }

  @SuppressWarnings("deprecation") @Override
  public void showImage(DraweeView imageView, Uri uri, @Nullable FrescoOption option) {
    if (uri.equals(Uri.EMPTY)) {
      imageView.setImageURI(null);
    } else {
      showImage(imageView, getBuilder(uri), option);
    }
  }

  @Override
  public void showImage(DraweeView imageView, @DrawableRes int id, @Nullable FrescoOption option) {
    showImage(imageView, getBuilder(id), option);
  }

  @Override public FrescoOption newOption(int resizeW, int resizeH) {
    FrescoOption option = new FrescoOption();
    option.setResizeOptions(new ResizeOptions(resizeW, resizeH));
    return option;
  }

  private ImageRequestBuilder getBuilder(Uri uri) {
    return ImageRequestBuilder.newBuilderWithSource(uri);
  }

  private ImageRequestBuilder getBuilder(@DrawableRes int resId) {
    return ImageRequestBuilder.newBuilderWithResourceId(resId);
  }

  private void showImage(DraweeView draweeView, ImageRequestBuilder builder, FrescoOption option) {
    if (option != null) {
      if (option.getResizeOptions() != null) {
        builder.setResizeOptions(option.getResizeOptions());
      }
    }
    ImageRequest request = builder.build();
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(request)
        .setOldController(draweeView.getController())
        .setAutoPlayAnimations(true)
        .build();

    draweeView.setController(controller);
  }

  public interface RequestCallback {
    void onSuccess(Bitmap bitmap, float currentPosition);
  }

  public static class FrescoOption implements ImageLoaderWrapper.ImageOption {
    private ResizeOptions mResizeOptions;

    public ResizeOptions getResizeOptions() {
      return mResizeOptions;
    }

    public void setResizeOptions(ResizeOptions resizeOptions) {
      mResizeOptions = resizeOptions;
    }
  }
}
