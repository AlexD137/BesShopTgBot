package ru.jmdevelop.besshoptgbot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.jmdevelop.besshoptgbot.main.ConfigReader;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.CartCommandHandler;
import ru.jmdevelop.besshoptgbot.main.TelegramBot;
import ru.jmdevelop.besshoptgbot.handlers.ActionHandler;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderConfirmCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderEnterAddressCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.CatalogCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderEnterCityCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderEnterNameCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderEnterPhoneNumberCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderStepCancelCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.OrderStepPreviousCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.StartCommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.register.CommandHandlerRegistry;
import ru.jmdevelop.besshoptgbot.handlers.commands.register.CommandHandlerRegistryDefault;
import ru.jmdevelop.besshoptgbot.repo.CartRepository;
import ru.jmdevelop.besshoptgbot.repo.CategoryRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientRepository;
import ru.jmdevelop.besshoptgbot.repo.OrderRepository;
import ru.jmdevelop.besshoptgbot.repo.ProductRepository;
import ru.jmdevelop.besshoptgbot.repo.database.CategoryRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.database.ClientRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.database.OrderRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.database.ProductRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.memory.CartRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.memory.ClientActionRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.memory.ClientCommandStateRepositoryDefault;
import ru.jmdevelop.besshoptgbot.repo.memory.ClientOrderStateRepositoryDefault;
import ru.jmdevelop.besshoptgbot.services.MessageService;
import ru.jmdevelop.besshoptgbot.services.NotificationService;
import ru.jmdevelop.besshoptgbot.services.impl.MessageServiceDefault;
import ru.jmdevelop.besshoptgbot.services.impl.NotificationServiceDefault;


public class BesShopTgBotApplication {

    private ConfigReader configReader = ConfigReader.getInstance();

    private ClientActionRepository clientActionRepository;
    private ClientCommandStateRepository clientCommandStateRepository;
    private ClientOrderStateRepository clientOrderStateRepository;
    private CartRepository cartRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ClientRepository clientRepository;

    private MessageService messageService;
    private NotificationService notificationService;

    private CommandHandlerRegistry commandHandlerRegistry;
    private List<CommandHandler> commandHandlers;
    private List<UpdateHandler> updateHandlers;
    private List<ActionHandler> actionHandlers;

    private void initializeRepositories() {
        clientActionRepository = new ClientActionRepositoryDefault();
        clientCommandStateRepository = new ClientCommandStateRepositoryDefault();
        clientOrderStateRepository = new ClientOrderStateRepositoryDefault();
        cartRepository = new CartRepositoryDefault();
        categoryRepository = new CategoryRepositoryDefault();
        productRepository = new ProductRepositoryDefault();
        orderRepository = new OrderRepositoryDefault();
        clientRepository = new ClientRepositoryDefault();
    }

    private void initializeServices() {
        messageService = new MessageServiceDefault();
        notificationService = new NotificationServiceDefault(configReader);
    }

    private void initializeCommandHandlers() {
        commandHandlerRegistry = new CommandHandlerRegistryDefault();
        commandHandlers = new ArrayList<>();

        commandHandlers.add(new CatalogCommandHandler(commandHandlerRegistry, categoryRepository, productRepository,
                cartRepository, messageService));

        commandHandlers.add(new CartCommandHandler(commandHandlerRegistry, clientCommandStateRepository,
                clientOrderStateRepository, cartRepository, clientRepository, messageService));

        commandHandlers.add(new OrderEnterNameCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        commandHandlers.add(new OrderEnterPhoneNumberCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        commandHandlers.add(new OrderEnterCityCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        commandHandlers.add(new OrderEnterAddressCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        commandHandlers.add(new OrderConfirmCommandHandler(clientActionRepository, clientCommandStateRepository,
                clientOrderStateRepository, cartRepository, orderRepository, clientRepository, messageService,
                notificationService));

        commandHandlerRegistry.setCommandHandlers(commandHandlers);
    }

    private void initializeUpdateHandlers() {
        updateHandlers = new ArrayList<>();

        updateHandlers.add(new StartCommandHandler(clientRepository, messageService));

        updateHandlers.add(new CatalogCommandHandler(commandHandlerRegistry, categoryRepository, productRepository,
                cartRepository, messageService));

        updateHandlers.add(new CartCommandHandler(commandHandlerRegistry, clientCommandStateRepository,
                clientOrderStateRepository, cartRepository, clientRepository, messageService));

        updateHandlers.add(new OrderStepCancelCommandHandler(clientActionRepository, clientCommandStateRepository,
                clientOrderStateRepository));

        updateHandlers.add(new OrderStepPreviousCommandHandler(commandHandlerRegistry, clientCommandStateRepository));

        updateHandlers.add(new OrderEnterPhoneNumberCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));
    }

    private void initializeActionHandlers() {
        actionHandlers = new ArrayList<>();

        actionHandlers.add(new OrderEnterNameCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        actionHandlers.add(new OrderEnterPhoneNumberCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        actionHandlers.add(new OrderEnterCityCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        actionHandlers.add(new OrderEnterAddressCommandHandler(commandHandlerRegistry, clientActionRepository,
                clientCommandStateRepository, clientOrderStateRepository));

        actionHandlers.add(new OrderConfirmCommandHandler(clientActionRepository, clientCommandStateRepository,
                clientOrderStateRepository, cartRepository, orderRepository, clientRepository, messageService,
                notificationService));
    }

    public static void main(String[] args) throws TelegramApiException {
        BesShopTgBotApplication application = new BesShopTgBotApplication();
        application.initializeRepositories();
        application.initializeServices();
        application.initializeCommandHandlers();
        application.initializeUpdateHandlers();
        application.initializeActionHandlers();

        TelegramBot telegramBot = new TelegramBot(application.configReader, application.clientActionRepository,
                application.updateHandlers, application.actionHandlers);

        new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
    }

}