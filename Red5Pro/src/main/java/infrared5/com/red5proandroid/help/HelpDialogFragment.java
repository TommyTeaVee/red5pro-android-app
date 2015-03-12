package infrared5.com.red5proandroid.help;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import infrared5.com.red5proandroid.BuildConfig;
import infrared5.com.red5proandroid.R;

public class HelpDialogFragment extends DialogFragment {

    public static HelpDialogFragment newInstance() {
        HelpDialogFragment fragment = new HelpDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HelpDialogFragment() {
        // Required empty public constructor
    }

    private void stylizeLinks(TextView textView) {
        CharSequence text = textView.getText();
        if(text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for(URLSpan url: urls) {
                URLSpan span = new URLSpan(url.getURL());
                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.rgb(226, 46, 0)), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(style);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_help_dialog, null, false);

        TextView helpOne = (TextView) v.findViewById(R.id.help_item_1);
        TextView helpTwo = (TextView) v.findViewById(R.id.help_item_2);
        TextView helpThree = (TextView) v.findViewById(R.id.help_item_3);
        TextView helpVersion = (TextView) v.findViewById(R.id.help_version);

        helpVersion.setText(BuildConfig.VERSION_NAME);
        String str1 = "<strong>1.</strong> In order to use this app you will need a working installation of <a href=\"http://red5pro.com\" style=\"color:#E21800\">Red5 Pro Server</a>.";
        String str2 = "<strong>2.</strong> You can use this app to stream live video from your device\'s camera, view another live stream, and/or connect to other Red5 Pro Second Screen applications for all kinds of experiences.";
        String str3 = "<p><strong>3.</strong> All the code for this app is live and free to use on <a href=\"https://github.com/infrared5/red5pro-example-apps\"><font color=\"#E21800\">GitHub</font></a>, and an overview of how it’s put together is available on our <a href=\"http://red5pro.com/docs\" style=\"color:#E21800\">site</a>.</p>";
        helpOne.setText(Html.fromHtml(str1));
        helpTwo.setText(Html.fromHtml(str2));
        helpThree.setText(Html.fromHtml(str3));

        helpOne.setMovementMethod(LinkMovementMethod.getInstance());
        helpOne.setLinksClickable(true);
        helpTwo.setMovementMethod(LinkMovementMethod.getInstance());
        helpTwo.setLinksClickable(true);
        helpThree.setMovementMethod(LinkMovementMethod.getInstance());
        helpThree.setLinksClickable(true);

        stylizeLinks(helpOne);
        stylizeLinks(helpTwo);
        stylizeLinks(helpThree);

        ContextThemeWrapper ctx = new ContextThemeWrapper(getActivity(), R.style.AppTheme );
        AlertDialog dialog =  new AlertDialog.Builder(ctx)
                .setView(v)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }

                })
                .create();

        return dialog;
    }

}
