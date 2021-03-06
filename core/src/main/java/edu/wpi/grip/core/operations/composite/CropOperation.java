package edu.wpi.grip.core.operations.composite;

import com.google.common.eventbus.EventBus;
import edu.wpi.grip.core.*;
import org.bytedeco.javacpp.opencv_core;

import java.io.InputStream;
import java.util.Optional;

import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

/**
 * @author Jaxon A Brown
 */
public class CropOperation implements Operation {
    private final SocketHint<opencv_core.Mat> inputHint = SocketHints.Inputs.createMatSocketHint("Input", false);
    private final SocketHint<opencv_core.Point> p1Hint = SocketHints.Inputs.createPointSocketHint("P1", false);
    private final SocketHint<opencv_core.Point> p2Hint = SocketHints.Inputs.createPointSocketHint("P2", false);

    private final SocketHint<opencv_core.Mat> outputHint = SocketHints.Inputs.createMatSocketHint("Output", true);

    @Override
    public String getName() {
        return "Crop";
    }

    @Override
    public String getDescription() {
        return "Crop an image";
    }

    @Override
    public Optional<InputStream> getIcon() {
        return Optional.of(getClass().getResourceAsStream("/edu/wpi/grip/ui/icons/grip.png"));
    }

    @Override
    public InputSocket<?>[] createInputSockets(EventBus eventBus) {
        return new InputSocket<?>[]{
                new InputSocket<>(eventBus, inputHint),
                new InputSocket<>(eventBus, p1Hint),
                new InputSocket<>(eventBus, p2Hint),
        };
    }

    @Override
    public OutputSocket<?>[] createOutputSockets(EventBus eventBus) {
        return new OutputSocket<?>[]{
                new OutputSocket<>(eventBus, outputHint)
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void perform(InputSocket<?>[] inputs, OutputSocket<?>[] outputs) {
        final opencv_core.Mat input = ((InputSocket<opencv_core.Mat>) inputs[0]).getValue().get();
        final opencv_core.Point p1 = ((InputSocket<opencv_core.Point>) inputs[1]).getValue().get();
        final opencv_core.Point p2 = ((InputSocket<opencv_core.Point>) inputs[2]).getValue().get();

        final OutputSocket<opencv_core.Mat> outputSocket = (OutputSocket<opencv_core.Mat>) outputs[0];
        final opencv_core.Mat output;

        opencv_core.Rect rect = new opencv_core.Rect(p1, p2);
        output = new opencv_core.Mat(input, rect);

        outputSocket.setValue(output);
    }
}