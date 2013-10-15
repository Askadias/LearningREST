package ru.forxy.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Concurrent executor implementation to run any task, that can depend on any other task
 */
public interface ITaskExecutor extends ExecutorService
{
    /**
     * Creates {@link ITaskStatusGroup} implementation whicg should be used to synchronized on
     * operations' groupd completion.
     *
     * @return ITaskStatusGroupGroup instance
     */
    ITaskStatusGroup createTasksGroup();

    /**
     * Runs all input tasks in separate threads and awaits they finished
     *
     * @param tasks - thread execution units contained base business logic
     * @param executionContext - data transfer object needs for dependent threads interaction
     * @return collection of execution futures to get access to execution results and context of every task execution
     *         like thrown exception i.e.
     */
    List<ITaskStatus> executeAll(List<? extends ITask> tasks, IExecutionContext executionContext);

    /**
     * Runs one input task in separate thread and awaits it's finished
     *
     * @param task - thread execution unit contained base business logic
     * @param executionContext - data transfer object needs for dependent threads interaction
     * @return execution future to get access to execution result and context of task execution like thrown exception
     *         i.e.
     */
    ITaskStatus execute(ITask task, IExecutionContext executionContext) throws InterruptedException;

    /**
     * Launches one input task in separate thread
     *
     * @param task - thread execution unit contained base business logic
     * @param executionContext - data transfer object needs for dependent threads interaction
     * @return execution future to get access to execution result and context of task execution like thrown exception
     *         i.e.
     */
    ITaskStatus launch(ITask task, IExecutionContext executionContext);

    /**
     * Launches one input task in separate thread
     *
     * @param task - thread execution unit contained base business logic
     * @param executionContext - data transfer object needs for dependent threads interaction
     * @param completeGroup -
     * @return execution future to get access to execution result and context of task execution like thrown exception
     *         i.e.
     */
    ITaskStatus launch(ITask task, IExecutionContext executionContext, ITaskStatusGroup completeGroup);
}
