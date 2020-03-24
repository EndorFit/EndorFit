package org.wmii.endorfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class timerDialog extends AppCompatDialogFragment {
    private EditText time;
    private timerDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(timerDialogListener) context;
        } catch (ClassCastException e) {
throw new ClassCastException(context.toString()+"must implement timer listener");        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.set_timer,null);
builder.setView(view);
builder.setTitle("Set timer");
builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
String seconds=time.getText().toString();
 listener.aplytext(seconds);
    }
});
time=view.findViewById(R.id.editTimer);
return builder.create();
    }
    public interface timerDialogListener{
void aplytext(String seconds);

    }
}
