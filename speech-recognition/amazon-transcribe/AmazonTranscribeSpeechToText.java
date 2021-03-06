// "jobName" is a self-descriptive string.
// For speech in US English, set "languageCode" to "en-US".
// "s3Uri" is a string pointing to an audio file stored in an S3 bucket.

AmazonTranscribe client = AmazonTranscribeClientBuilder
    .standard()
    .withCredentials(credentialsProvider)
    .withRegion(Regions.US_EAST_1)
    .build();

Media media = new Media().withMediaFileUri(s3Uri);

StartTranscriptionJobRequest startRequest = new StartTranscriptionJobRequest()
    .withTranscriptionJobName(jobName)
    .withLanguageCode(languageCode)
    .withMedia(media)
    .withMediaFormat(MediaFormat.Mp3.toString());

client.startTranscriptionJob(startRequest);

GetTranscriptionJobRequest getRequest = new GetTranscriptionJobRequest().withTranscriptionJobName(jobName);

while (true) {
    GetTranscriptionJobResult getResponse = client.getTranscriptionJob(getRequest);

    TranscriptionJob job = getResponse.getTranscriptionJob();

    if (job.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.toString())) {

        Transcript transcript = job.getTranscript();

        String transcriptUri = transcript.getTranscriptFileUri();
        System.out.println(transcriptUri);

        break;
    }
    else {
        Thread.sleep(1000L);
    }
}
