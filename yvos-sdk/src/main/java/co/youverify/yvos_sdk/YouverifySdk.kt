package co.youverify.yvos_sdk

/**
 * The entry point to initialize and access any of the SDK services.
 */
object YouverifySdk {

    /**
     * Initialize the Vform Module. You can thereafter call [start] on the returned VFormModule instance to start the Vform service.
     * @param option holds the information required by the Vform service.
     * @return returns an instance of "VFormModule" with the specified option.
     */
    //fun vFormModule(option: VFormOption): VFormModule = VFormModule(option = option)
    /**
     * Initialize the Liveness check Module. You can thereafter call [start] on the returned LivenessCheckModule instance to start the liveness check service.
     * @param option holds the information required by the liveness check service.
     * @return returns an instance of "LivenessCheckModule" with the specified option.
     */
    //fun livenessModule(option: LivenessOption): LivenessCheckModule =LivenessCheckModule(option=option)

    /**
     * Initialize the Document capture Module. You can thereafter call [start] on the returned DocumentCaptureModule instance to start the document capture service.
     * @param option holds the information required by the document capture service.
     * @return returns an instance of "DocumentCaptureModule" with the specified option.
     */
    //fun documentCaptureModule(option: DocumentOption): DocumentCaptureModule = DocumentCaptureModule( option =option)
}