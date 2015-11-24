package viewlib.rainhong.bloodprogressviewlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

public class BloodProgressView extends View {
    private float View_Height;
    private float View_Width;

    private boolean XAxisDrawVisible;
    private boolean YAxisDrawVisible;
    private boolean IndicatorDrawVisible;

    private Paint XYAxisLabelLinePaint;
    private Paint RectProgressPaint;
    private Paint ValueIndicatorPaint;

    private float CutValue;
    private float Y_Range;
    private float CurrentIndex;
    private int Indicator_Left;
    private int Indicator_Right;
    private Rect BloodProgressRect;
    private int Rect_Left;
    private int Rect_Right;
    private int Rect_Bottom;
    private int Rect_Top;

    private float PercentValue;
    private float PercentValue_Current;
    private int ProgressColor;
    private int ProgressColor_alpha;
    private int ProgressIndicatorColor;
    private int ProgressIndicatorColor_alpha;
    private int ProgressXYAxisLabelLineColor;
    private int ProgressXYAxisLabelLineColor_alpha;
    private boolean Flag_ColorToBlood;

    private ValueAnimator ValueAnimation;
    private int AnimationTime;

    public BloodProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBloodProgressView();
    }

    private void initBloodProgressView() {
        PercentValue = 0;
        ProgressColor = Color.RED;
        ProgressColor_alpha = 150;

        ProgressIndicatorColor = Color.BLACK;
        ProgressIndicatorColor_alpha = 255;

        ProgressXYAxisLabelLineColor = Color.BLACK;
        ProgressXYAxisLabelLineColor_alpha = 255;

        XYAxisLabelLinePaint = new Paint();
        RectProgressPaint = new Paint();
        ValueIndicatorPaint = new Paint();
        BloodProgressRect = new Rect();
        XYAxisLabelLinePaint.setStrokeWidth(5);
        ValueIndicatorPaint.setStrokeWidth(5);

        Rect_Left = Rect_Right = Rect_Top = Rect_Bottom = 0;
        Indicator_Left = Indicator_Right = 0;
        Y_Range = CurrentIndex = 0;

        AnimationTime = 1000;
        ValueAnimation = null;

        Flag_ColorToBlood = false;
        XAxisDrawVisible = true;
        YAxisDrawVisible = true;
        IndicatorDrawVisible = true;
    }

    /**
     * Set Blood Progress View Color.
     * @param colors Blood Progress Color.
     */
    public void setProgressColor(int colors) {
        ProgressColor = colors;
    }

    /**
     * Set Blood Progress View Indicator Color.
     * @param colors Blood Progress Indicator Color.
     */
    public void setProgressIndicatorColor(int colors) {
        ProgressIndicatorColor = colors;
    }

    /**
     * Set Blood Progress View XYAxis Label Color.
     * @param colors Blood Progress XYAxis Label Color.
     */
    public void setProgressXYAxisLabelLineColor(int colors) {
        ProgressXYAxisLabelLineColor = colors;
    }

    /**
     * Set Blood Progress View alpha.
     * @param alphas Blood Progress alpha. Value: between 0 and 255.
     */
    public void setProgressColor_alpha(int alphas) {
        if(alphas < 0 || alphas > 255) return;
        ProgressColor_alpha = alphas;
    }

    /**
     * Set Blood Progress View Indicator alpha.
     * @param alphas Blood Progress Indicator alpha. Value: between 0 and 255.
     */
    public void setProgressIndicatorColor_alpha(int alphas) {
        if(alphas < 0 || alphas > 255) return;
        ProgressIndicatorColor_alpha = alphas;
    }

    /**
     * Set Blood Progress View XYAxisLabelLine alpha.
     * @param alphas Blood Progress XYAxisLabelLine alpha. Value: between 0 and 255.
     */
    public void setProgressXYAxisLabelLineColor_alpha(int alphas) {
        if(alphas < 0 || alphas > 255) return;
        ProgressXYAxisLabelLineColor_alpha = alphas;
    }

    /**
     * Get Blood Progress Current Value.
     * @return Blood Progress Current Value.
     */
    public int getPercentValue() {
        return (int)this.PercentValue_Current;
    }

    /**
     * Set Blood Progress View Color Animation Enable or Not. Value: 100-50-0, Green, Yellow, Red.
     * @param Flag Enable Animation True or False.
     */
    public void setColorToBlood(boolean Flag) {
        this.Flag_ColorToBlood = Flag;
    }

    /**
     * Set Blood Progress View X Axis Label Visible.
     * @param Flag Visible true or false. Default: true.
     */
    public void setXAxisDrawVisible(boolean Flag) {
        this.XAxisDrawVisible = Flag;
    }

    /**
     * Set Blood Progress View Y Axis Label Visible.
     * @param Flag Visible true or false. Default: true.
     */
    public void setYAxisDrawVisible(boolean Flag) {
        this.YAxisDrawVisible = Flag;
    }

    /**
     * Set Blood Progress View Indicator Visible.
     * @param Flag Visible true or false. Default: true.
     */
    public void setIndicatorDrawVisible(boolean Flag) {
        this.IndicatorDrawVisible = Flag;
    }

    private void setPercentValue(int PercentValue) {
        if(0 <= PercentValue && PercentValue <= 100) {
            this.PercentValue = PercentValue;
            invalidate();
        }
    }

    /**
     * Set Blood Progress Value and Animation Progress Value;
     * @param PercentValue Set Blood Progress Value. Value: Between 0 and 100.
     * @param AnimationFlag Enable and Start Animation.
     */
    public void setPercentValue(int PercentValue, boolean AnimationFlag) {
        if(0 <= PercentValue && PercentValue <= 100) {
            this.PercentValue_Current = PercentValue;
            if(AnimationFlag) {
                this.ProgressAnimation(AnimationTime, 0, PercentValue);
            }
            else
                this.setPercentValue(PercentValue);
        }
    }

    /**
     * Set Default Blood Progress Refresh Animation Time.
     * @param MsTime Set Refresh Animation Time. Value: Millisecond. (1000ms = 1s) Default: 1000ms
     */
    public void setDefaultAnimationTime(int MsTime) {
        this.AnimationTime = MsTime;
    }

    /**
     * Progress Animation.
     * @param MsTime Animation Time.
     * @param AnimationStartValue Animation Start Value.
     * @param AnimationStopValue Animation Stop Value.
     */
    public void ProgressAnimation(int MsTime, int AnimationStartValue, int AnimationStopValue) {
        if(ValueAnimation != null)
            if(ValueAnimation.isRunning())
                ValueAnimation.cancel();
        ValueAnimation = ValueAnimator.ofInt(AnimationStartValue, AnimationStopValue);
        ValueAnimation.setInterpolator(AnimationUtils.loadInterpolator(this.getContext(), android.R.anim.decelerate_interpolator));
        ValueAnimation.setDuration(MsTime);
        ValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setPercentValue(Integer.parseInt(animation.getAnimatedValue().toString()));
            }
        });
        ValueAnimation.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        View_Height = h;
        View_Width = w;
        CutValue = View_Width/10;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        XYAxisLabelLinePaint.setColor(ProgressXYAxisLabelLineColor);
        XYAxisLabelLinePaint.setAlpha(ProgressXYAxisLabelLineColor_alpha);
        if(XAxisDrawVisible)
            canvas.drawLine(0, View_Height - CutValue, View_Width, View_Height - CutValue, XYAxisLabelLinePaint);
        if(YAxisDrawVisible) {
            canvas.drawLine(CutValue, 0, CutValue, View_Height, XYAxisLabelLinePaint);
            Y_Range = (View_Height - 2*CutValue)/4;
            CurrentIndex = CutValue;
            for(int i=0;i<4;i++) {
                canvas.drawLine(CutValue, CurrentIndex, 2 * CutValue, CurrentIndex, XYAxisLabelLinePaint);
                CurrentIndex += Y_Range;
            }
        }

        Rect_Left = (int)(4 * CutValue + CutValue / 2);
        Rect_Right = (int)(8*CutValue + CutValue/2);
        Rect_Bottom = (int)(View_Height-CutValue-2);
        Rect_Top = (int)(CutValue + (View_Height - 2*CutValue)*(1 - PercentValue/100));
        this.BloodProgressRect.set(Rect_Left, Rect_Top, Rect_Right, Rect_Bottom);

        if(!Flag_ColorToBlood) {
            RectProgressPaint.setColor(ProgressColor);
            RectProgressPaint.setAlpha(ProgressColor_alpha);
        }
        else {
            int ColorTurnIndex;
            if(51 <= PercentValue && PercentValue <= 100) {
                ColorTurnIndex = (int)(PercentValue-50)*5;
                if(ColorTurnIndex > 255) ColorTurnIndex = 255;
                else if(ColorTurnIndex < 0) ColorTurnIndex = 0;
                RectProgressPaint.setARGB(255, 255 - ColorTurnIndex, 255, 0);
            }
            else if(0 <= PercentValue && PercentValue <= 50) {
                ColorTurnIndex = (int)PercentValue*5;
                if (ColorTurnIndex > 255) ColorTurnIndex = 255;
                else if(ColorTurnIndex < 0) ColorTurnIndex = 0;
                RectProgressPaint.setARGB(255, 255, ColorTurnIndex, 0);
            }
        }

        RectProgressPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(BloodProgressRect, RectProgressPaint);

        if(PercentValue > 0) {
            if(IndicatorDrawVisible) {
                ValueIndicatorPaint.setColor(ProgressIndicatorColor);
                ValueIndicatorPaint.setAlpha(ProgressIndicatorColor_alpha);
                Indicator_Left = (int) (3 * CutValue - CutValue / 2);
                Indicator_Right = (int) (Rect_Right + CutValue);
                canvas.drawLine(Indicator_Left, Rect_Top, Indicator_Right, Rect_Top, ValueIndicatorPaint);
                canvas.drawLine(Indicator_Left, Rect_Top, Indicator_Left + CutValue, Rect_Top - CutValue / 2, ValueIndicatorPaint);
                canvas.drawLine(Indicator_Left, Rect_Top, Indicator_Left + CutValue, Rect_Top + CutValue / 2, ValueIndicatorPaint);
            }
        }
    }
}
