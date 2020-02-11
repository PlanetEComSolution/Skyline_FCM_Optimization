package planet.info.skyline.crash_report;

import android.content.Context;

/*import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;*/

import planet.info.skyline.controller.AppController;

/*@ReportsCrashes(formKey = "",
       // formUri = "https://docs.google.com/spreadsheets/d/19TevDhKHKDPhQWqi17MyvPLR8duCSToeRKlsj9BYwXU/edit#gid=0",
       mailTo = "nkumar@planetecomsolutions.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
        //mode = ReportingInteractionMode.SILENT
)*/

public class CrashReport extends AppController {
    private static Context cont;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // The following line triggers the initialization of ACRA

            //   cont=getApplicationContext();
         //   ACRA.init(this);

            //  ACRA.getErrorReporter().setReportSender(new LocalReportSender(this));

            // instantiate the report sender with the email credentials.
            // these will be used to send the crash report
          //  ACRAReportSender reportSender = new ACRAReportSender("nvnkmr1290@gmail.com", "princess9089");

            // register it with ACRA.
           // ACRA.getErrorReporter().setReportSender(reportSender);


        } catch (Exception er) {
            er.getMessage();
        }


    }

    public static Context getCont() {
        return cont;
    }
}
