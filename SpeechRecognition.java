import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpeechRecognition {
    // This example requires environment variables named "SPEECH_KEY" and "SPEECH_REGION"
    private static String speechKey = System.getenv("SPEECH_KEY");
    private static String speechRegion = System.getenv("SPEECH_REGION");

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
        speechConfig.setSpeechRecognitionLanguage("en-US");
        recognizeFromMicrophone(speechConfig);
    }

    public static void recognizeFromMicrophone(SpeechConfig speechConfig) throws InterruptedException, ExecutionException {
        AudioConfig audioConfig = AudioConfig.fromWavFileInput("./samples/one-voice-some-static.wav");
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);

        System.out.println("Speak into your microphone.");
        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult speechRecognitionResult = task.get();

        if (speechRecognitionResult.getReason() == ResultReason.RecognizedSpeech) {
            System.out.println("RECOGNIZED: Text=" + speechRecognitionResult.getText());
        }
        else if (speechRecognitionResult.getReason() == ResultReason.NoMatch) {
            System.out.println("NOMATCH: Speech could not be recognized.");
        }
        else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you set the speech resource key and region values?");
            }
        }

        System.exit(0);
    }
}
