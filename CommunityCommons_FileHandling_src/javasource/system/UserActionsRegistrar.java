package system;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.mendix.core.Core;
import com.mendix.core.component.LocalComponent;
import com.mendix.core.component.MxRuntime;
import com.mendix.integration.Integration;

@Component(immediate = true, properties = {"event.topics:String=com/mendix/events/model/loaded"})
public class UserActionsRegistrar implements EventHandler
{
	private MxRuntime mxRuntime;
	private LocalComponent component;
	private Integration integration;
	
	@Reference
	public void setMxRuntime(MxRuntime runtime)
	{
		mxRuntime = runtime;
		mxRuntime.bundleComponentLoaded();
	}
	
	@Reference
	public void setIntegration(Integration integration)
	{
		this.integration = integration;
	}
	
	@Override
	public void handleEvent(Event event)
	{
		if (event.getTopic().equals(com.mendix.core.event.EventConstants.ModelLoadedTopic()))        
		{
			component = mxRuntime.getMainComponent();
			Core.initialize(component, integration);   
			component.actionRegistry().registerUserAction(appcloudservices.actions.GenerateRandomPassword.class);
			component.actionRegistry().registerUserAction(appcloudservices.actions.LogOutUser.class);
			component.actionRegistry().registerUserAction(appcloudservices.actions.StartSignOnServlet.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.Base64DecodeToFile.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.Base64EncodeFile.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.DuplicateFileDocument.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.DuplicateImageDocument.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.FileDocumentFromFile.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.FileFromFileDocument.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.GetFileContentsFromResource.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.getFileSize.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.GetImageDimensions.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.storeURLToFileDocument.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.StringFromFile.class);
			component.actionRegistry().registerUserAction(coco_filehandling.actions.StringToFile.class);
			component.actionRegistry().registerUserAction(system.actions.VerifyPassword.class);
		}
	}
}