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
public class PlaceRectangleOperation implements Operation {
    private final SocketHint<opencv_core.Mat> inputHint = SocketHints.Inputs.createMatSocketHint("Input", false);
    private final SocketHint<opencv_core.Point> p1Hint = SocketHints.Inputs.createPointSocketHint("P1", false);
    private final SocketHint<opencv_core.Point> p2Hint = SocketHints.Inputs.createPointSocketHint("P2", false);
    private final SocketHint<Color> colorHint = SocketHints.createEnumSocketHint("Color", Color.RED);

    private final SocketHint<opencv_core.Mat> outputHint = SocketHints.Inputs.createMatSocketHint("Output", true);

    private enum Color {
        WHITE("White", opencv_core.AbstractScalar.WHITE),
        GRAY("Gray", opencv_core.AbstractScalar.GRAY),
        BLACK("Black", opencv_core.AbstractScalar.BLACK),
        RED("Red", opencv_core.AbstractScalar.RED),
        GREEN("Green", opencv_core.AbstractScalar.GREEN),
        BLUE("Blue", opencv_core.AbstractScalar.BLUE),
        CYAN("Cyan", opencv_core.AbstractScalar.CYAN),
        MAGENTA("Magenta", opencv_core.AbstractScalar.MAGENTA),
        YELLOW("Yellow", opencv_core.AbstractScalar.YELLOW);

        private final String label;
        private final opencv_core.Scalar val;

        Color(String label, opencv_core.Scalar val) {
            this.label = label;
            this.val = val;
        }

        public opencv_core.Scalar getVal() {
            return this.val;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    @Override
    public String getName() {
        return "PlaceRectangle";
    }

    @Override
    public String getDescription() {
        return "Place a rectangle over the image.";
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
                new InputSocket<>(eventBus, colorHint)
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
        final Color color = ((InputSocket<Color>) inputs[3]).getValue().get();

        final OutputSocket<opencv_core.Mat> outputSocket = (OutputSocket<opencv_core.Mat>) outputs[0];
        final opencv_core.Mat output = outputSocket.getValue().get();

        input.copyTo(output);
        rectangle(output, p1, p2, color.getVal(), 2, 1, 0);

        outputSocket.setValue(output);
    }
}