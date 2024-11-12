package com.example.agriculturaltrade.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.agriculturaltrade.R;

// PagerAdapter sử dụng để hiển thị các view tùy ý trong ViewPager
public class SliderAdapter extends PagerAdapter {

    Context context;

    // LayoutInflater sử dụng để tạo view tu xml
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int imagesArray[] = {
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3,
    };

    int headingArray[] = {
            R.string.first_slide,
            R.string.second_slide,
            R.string.third_slide,
    };

    int descriptionArray[] = {
            R.string.description1,
            R.string.description2,
            R.string.description3,
    };

    // Phương thức getCount() trả về số lượng item trong Adapter
    // Tại đây trả về số lượng item trong mảng headingArray
    @Override
    public int getCount() {
        return headingArray.length;
    }

    // Phương thức isViewFromObject() trả về boolean
    // Nó được sử dụng để kiểm tra xem view và object có khớp nhau hay không
    // Tại đây kiểm tra xem view có phải là ConstraintLayout và object có phải là ConstraintLayout hay không
    // Nếu khớp nhau thì trả về true
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        // Lấy đối tượng LayoutInflater từ context
        // LayoutInflater sử dụng để convert 1 layout xml thành 1 view
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Sử dụng LayoutInflater để convert layout sliding_layout.xml thành 1 view
        // inflate(int resource, ViewGroup root) sẽ trả về 1 view
        // Tham số resource là layout xml cần convert
        // Tham số root là 1 ViewGroup cha, sẽ được set cho view mới tạo
        // Tham số attachToRoot là 1 boolean, nếu là true, view mới tạo sẽ được add vào root
        // Nếu là false, view mới tạo không được add vào root
        View view = layoutInflater.inflate(R.layout.sliding_layout,container,false);

        ImageView imageView = view.findViewById(R.id.slider_img);
        TextView heading = view.findViewById(R.id.heading);
        TextView description = view.findViewById(R.id.description);

        imageView.setImageResource(imagesArray[position]);
        heading.setText(headingArray[position]);
        description.setText(descriptionArray[position]);

        container.addView(view);

        return view;
    }

    @Override
    /**
     * Destroy item là 1 method của PagerAdapter
     * Method này được gọi khi 1 page được remove khỏi ViewPager
     * Tham số container là 1 ViewGroup cha của page
     * Tham số position là vị trí của page trong ViewPager
     * Tham số object là page được remove
     * Công việc của method này là remove page khỏi container
     */
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}